package org.apache.mina.db;

import org.apache.log4j.PropertyConfigurator;

public class Main
{

	static 
	{ 
		System.setProperty("org.jboss.logging.provider", "log4j");
	}
	
	private static final String EHCACHE_CONFIG = "resource/ehcache.xml";
	private static final String LOG4J_CONFIG   = "resource/log4j.properties";
	
	public static void GlobalConfig()
	{
		PropertyConfigurator.configure(LOG4J_CONFIG);
		
		//EHCache
		//CacheManager manager = CacheManager.create(EHCACHE_CONFIG);
	}

	public static void main(String[] args) 
	{
		GlobalConfig();
		
	}

}
