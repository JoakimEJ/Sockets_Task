package Client;

import DBFunctionality.HighScore;
import PropertiesManager.PropertiesManager;
import Question.Question;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Joakim on 26.11.2016.
 */
public class UserClient implements Runnable
{
	private static final PropertiesManager props = new PropertiesManager();
	private Object objectFromGame;
	private ObjectOutputStream toGame;
	private ObjectInputStream fromGame;
	private double score;
	private Scanner scanner;

	public UserClient()
	{
		String host = props.getHost();
		int port = props.getPort();
		// Create the input/output streams to the server
		try
		{
			Socket clientSocket = new Socket(host, port);
			toGame = new ObjectOutputStream(clientSocket.getOutputStream());
			fromGame = new ObjectInputStream(clientSocket.getInputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		score = 0;
		scanner = null;
	}

	@Override
	public void run ()
	{
		startGame();
	}

	private void startGame()
	{
		try
		{
			objectFromGame = fromGame.readObject();
			// Print Welcome text from server
			if(objectFromGame instanceof String)
			{
				System.out.println(objectFromGame);

			}
			// Send response
			String myResponse = getUserAnswer();
			if(myResponse.matches(".+") && !myResponse.equals("0"))
			{
				toGame.writeObject(myResponse);
			}
			else if (myResponse.equals("0"))
			{
				System.out.println("Ok.. No problem.. BYE!");
				toGame.writeObject("0");
				return;
			}

			mainClientLoop();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void mainClientLoop () throws Exception
	{
		objectFromGame = fromGame.readObject();
		if (objectFromGame instanceof Question)
		{
			Question tempQuestion = (Question)objectFromGame;
			System.out.println(tempQuestion.getSpm() + "\n");
			String userAnswer = getUserAnswer();
			if (userAnswer.equals("0"))
			{
				toGame.writeObject("0");
			}
			else
			{
				if (checkAnswer(userAnswer, tempQuestion.getSvar()))
				{
					score = (score + 1)*1.25;
					System.out.println("Correct. You are a genius! Have another one!\n" +
							"--------------------------------------------");
				}
				else
				{
					System.out.println("Wrong, sorry (not sorry). Here is another one, good luck :)\n");
					score = score*0.75;
				}
				toGame.writeObject("1"); // Signals to the Game that we want to continue
			}
			mainClientLoop();
		}
		else if (objectFromGame instanceof String)
		{
			if (((String) objectFromGame).startsWith("Thank you for playing"))
			{
				System.out.println(objectFromGame);
				String answer = getUserAnswer();
				if (answer.equalsIgnoreCase("y"))
				{
					HighScore myHighScore = new HighScore();
					myHighScore.setScore(score);
					toGame.writeObject(myHighScore);
					// Get HighScore-list and print to terminal
					objectFromGame = fromGame.readObject();
					ArrayList<HighScore> highScores = (ArrayList<HighScore>) objectFromGame;
					printListToTerminal(highScores);
				}
				else
				{
					toGame.writeObject("as long as this is not HighScore everything is fine");
				}
			}
			else if (((String) objectFromGame).startsWith("Oops"))
			{
				System.out.println(objectFromGame);
			}
			closeResources();
		}
	}

	private boolean checkAnswer(String userAnswer, String CorrectAnswer)
	{
		boolean answerIsCorrect = false;
		if(userAnswer.toLowerCase().equals(CorrectAnswer.toLowerCase()))
		{
			answerIsCorrect = true;
		}
		return answerIsCorrect;
	}

	public void printListToTerminal(ArrayList<HighScore> listToPrint)
	{
		listToPrint.sort((o1, o2) -> Double.compare(o2.getScore(), o1.getScore()));

		int max;
		if (listToPrint.size() > 10)
			max = 10;
		else
			max = listToPrint.size();

		for (int i = 0; i < max; i++)
		{
			System.out.println(i+1 + ". " + listToPrint.get(i));
		}
	}

	private String getUserAnswer ()
	{
		String myAnswer = null;
		try
		{
			scanner = new Scanner(System.in);
			System.out.println("Enter your answer to the question: ");
			myAnswer = scanner.nextLine();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return myAnswer;
	}

	public void closeResources()
	{
		scanner.close();
		try
		{
			fromGame.close();
			toGame.close();
		}
		catch (IOException e)
		{
			System.out.println("Something went wrong with the closeResources method. Yikes!");
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		new UserClient().run();
	}

}
