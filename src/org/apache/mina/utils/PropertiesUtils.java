package org.apache.mina.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtils
{

	public static Map<String, String> LoadProperties(String pathFile)
	{
		Map<String, String> map = new HashMap<String, String>();
		try
		{
			Properties prop = new Properties();
			InputStream in = Object.class.getResourceAsStream(pathFile);
			prop.load(in);
			for (Enumeration e = prop.propertyNames(); e.hasMoreElements();)
			{
				String key = (String) e.nextElement();
				String value = prop.getProperty(key).trim();
				map.put(key, value);
			}

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return map;
	}

}
