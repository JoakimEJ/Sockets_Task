package DBFunctionality;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import Question.*;

/**
 * Created by Joakim on 25.11.2016.
 */
public class ORMLite
{
	private ConnectionManager connectionManager = new ConnectionManager();
	private ConnectionSource source;
	private QuestionManager questionManager;

	// Declare the spmDao
	private Dao<Question, Integer> spmDao;

	@SuppressWarnings("unchecked")
	public ORMLite ()
	{
		try
		{
			source = connectionManager.getConnectionSource();
			// Instantiate the dao
			spmDao = DaoManager.createDao(source, Question.class);
			// Instantiate the questionManager
			questionManager = new QuestionManager();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void createAndPopulateTables ()
	{
		createTable(source);
		questionManager.populateQuestionTable(spmDao);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Question> getQuestionList()
	{
		ArrayList<Question> tempList;

		try
		{
			tempList = (ArrayList) spmDao.queryForAll();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			tempList = null;
		}
		return tempList;
	}

	private void createTable (ConnectionSource source)
	{
		try
		{
			// Create tables if needed
			TableUtils.createTableIfNotExists(source, Question.class);
			TableUtils.createTableIfNotExists(source, HighScore.class);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				source.close();
			}
			catch (IOException f)
			{
				f.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws Exception
	{
		new ORMLite();
	}

}
