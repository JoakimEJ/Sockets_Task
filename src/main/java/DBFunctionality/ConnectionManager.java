package DBFunctionality;

import PropertiesManager.PropertiesManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by Joakim on 09.12.2016.
 */
public class ConnectionManager
{
	private static final PropertiesManager props = new PropertiesManager();

	private String databaseUrl;
	private String user;
	private String password;

	public ConnectionManager ()
	{
		databaseUrl = props.getDatabaseUrl();
		user = props.getUser();
		password = props.getPassword();
	}

	public ConnectionSource getConnectionSource()
	{
		ConnectionSource connectionSource;
		try
		{
			connectionSource = new JdbcConnectionSource(databaseUrl, user, password);

		}
		catch (SQLException e)
		{
			System.out.println("getConnectionSource failed..");
			e.printStackTrace();
			connectionSource = null;
		}
		return connectionSource;
	}
}
