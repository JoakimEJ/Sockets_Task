package PropertiesManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Joakim on 09.12.2016.
 */
public class PropertiesManager
{
	private HashMap<String, String> allProperties;

	public PropertiesManager()
	{
		allProperties = getAllProperties();
	}

	// Getters

	public int getPort()
	{
		return Integer.parseInt(allProperties.get("port"));
	}

	public String getHost()
	{
		return allProperties.get("host");

	}

	public String getUser()
	{
		return allProperties.get("user");
	}

	public String getPassword()
	{
		return allProperties.get("password");
	}

	public String getDatabaseUrl()
	{
		return allProperties.get("databaseUrl");
	}

	private HashMap<String, String> getAllProperties()
	{
		Properties prop = new Properties();
		FileInputStream input;
		HashMap<String, String> allProperties = new HashMap<>();

		try
		{
			String filename = "src/main/resources/config.properties";
			//input = getClass().getClassLoader().getResourceAsStream(filename);
			input = new FileInputStream(filename);
			if (input == null)
			{
				System.out.println("Could not find: " + filename);
				return null;
			}

			prop.load(input);

			Enumeration<?> allProps = prop.propertyNames();
			while(allProps.hasMoreElements())
			{
				String key = (String) allProps.nextElement();
				String value = prop.getProperty(key);
				allProperties.put(key, value);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		return allProperties;
	}
}
