package QuizGame;

import DBFunctionality.ConnectionManager;
import DBFunctionality.HighScore;
import Question.Question;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Joakim on 29.11.2016.
 * Each client that connects to the Server will start a new Thread which runs this class
 */
public class QuizGame implements Runnable
{
	private Socket clientSocket;
	private ObjectInputStream inputFromClient;
	private ObjectOutputStream outputToClient;
	private Object objectFromClient;
	private Random rand = new Random();
	private Dao<HighScore, Double> highScoreDao;
	private ArrayList<Question> questions;
	private String userName;

	public QuizGame(Socket clientSocket, ArrayList<Question> questions)
	{
		ConnectionManager connectionManager = new ConnectionManager();
		ConnectionSource source = connectionManager.getConnectionSource();
		this.clientSocket = clientSocket;
		this.questions = questions;

		try{ highScoreDao = DaoManager.createDao(source, HighScore.class); }
		catch (SQLException e) { e.printStackTrace(); }
	}

	@Override
	public void run () { startQuizGame(clientSocket); }

	public void startQuizGame(Socket clientSocket)
	{
		try
		{
			inputFromClient = new ObjectInputStream(clientSocket.getInputStream());
			outputToClient = new ObjectOutputStream(clientSocket.getOutputStream());

			String header1 = "##################################################\n" +
					"Hello and welcome to the QUIZ game of your dreams!\n" +
					"##################################################\n\n" +
					"Submit a username to start quiz, or 0(zero) if you want to exit.";
			outputToClient.writeObject(header1);

			// Get response from client
			objectFromClient = inputFromClient.readObject();
			// Act on response
			if(objectFromClient instanceof String)
			{
				if(((String) objectFromClient).matches(".+") && !objectFromClient.equals("0"))
				{
					userName = (String)objectFromClient;
					mainGameLoop();
				}
				else if(objectFromClient.equals("0"))
				{
					// Do nothing..
				}
				else
				{
					outputToClient.writeObject("Oops. Something went wrong when checking username. " +
							"Big Canadian sorry to you.\n" +
							"Try restarting your client and enter a simple username (like Bob.. Bob Ross!)");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void mainGameLoop() throws Exception
	{
		boolean isRunning = true;
		while(isRunning)
		{
			outputToClient.writeObject(questions.get(rand.nextInt(questions.size())));
			objectFromClient = inputFromClient.readObject();

			if (objectFromClient instanceof String)
			{
				if (objectFromClient.equals("0"))
				{
					outputToClient.writeObject("Thank you for playing, you are a wonderful human being!\n" +
							"Would you like to save your score, and print the top 10 list? (y/n)");

					setHighScoreIfWanted();
					isRunning = false;
				}
			}
		}
	}

	private void setHighScoreIfWanted () throws Exception
	{
		objectFromClient = inputFromClient.readObject();
		if (objectFromClient instanceof HighScore)
		{
			HighScore highScore = (HighScore) objectFromClient;
			highScore.setName(userName);
			highScoreDao.create(highScore);

			// Send list of highscores to client
			ArrayList<HighScore> highScores = (ArrayList<HighScore>) highScoreDao.queryForAll();
			outputToClient.writeObject(highScores);
		}
	}
}
