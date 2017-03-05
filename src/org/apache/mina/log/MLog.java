package org.apache.mina.log;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MLog
{
	public static Logger logger = Logger.getLogger (MLog.class.getName());
	
	static
	{
		//BasicConfigurator.configure();
		PropertyConfigurator.configure( "config/log4j.properties");
		
	}
	
	public static void Debug(String format,Object... message)
	{
		String fMessage = String.format(format, message);
		logger.debug(fMessage);
	}

	public static void Warn(String format,Object... message)
	{
		String fMessage = String.format(format, message);
		logger.warn(fMessage);
	}
	
	public static void Error(String format,Object... message)
	{
		String fMessage = String.format(format, message);
		logger.error(fMessage);
	}
	
}
