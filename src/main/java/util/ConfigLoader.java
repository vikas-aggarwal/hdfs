package util;

import java.io.IOException;
import java.util.Properties;

public class ConfigLoader 
{
	
	public static String fsImagePath;
	public static Integer replicationFactor;
	public static String blockCounterFile;
	
	public static void loadConfig() throws IOException
	{
		Properties prop = new Properties();
		prop.load(ConfigLoader.class.getResourceAsStream("config.properties"));
		
		fsImagePath = prop.getProperty("fsImagePath");
		replicationFactor = Integer.parseInt(prop.getProperty("replicationFactor"));
		blockCounterFile = prop.getProperty("blockNumberCounterFile");
	}
}
