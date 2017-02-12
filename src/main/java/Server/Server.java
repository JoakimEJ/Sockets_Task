package Server;

import DBFunctionality.ORMLite;
import PropertiesManager.PropertiesManager;
import Question.Question;
import QuizGame.QuizGame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Joakim on 26.11.2016.
 *
 * This class listen for incoming clients and starts a new Quizgame for each of the connected clients
 * as long as the limit of the threadPool is not met.
 */
public class Server
{
	private static final PropertiesManager props = new PropertiesManager();
	private ServerSocket welcomeSocket;
	private ExecutorService threadPool;
	private ORMLite ormLite;
	private ArrayList<Question> questions;

	public Server ()
	{
		threadPool = Executors.newFixedThreadPool(50);
		ormLite = new ORMLite();
		ormLite.createAndPopulateTables();
		questions = ormLite.getQuestionList();

		try
		{
			int port = props.getPort();
			this.welcomeSocket = new ServerSocket(port);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public Socket listenForClients() throws IOException
	{

		return welcomeSocket.accept();
	}

	public static void main(String[] args)
	{
		Server s = new Server();
		int connectionCounter = 0;

		System.out.println("Listening");
		while (true)
		{
			try
			{
				Socket clientSocket = s.listenForClients();
				System.out.println("Client nr: " + ++connectionCounter + " connected");
				s.threadPool.execute(new QuizGame(clientSocket, s.questions));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
	}
}
