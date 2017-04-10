package org.apache.mina.tcp.base.logicserver;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;

public class LogicMain
{
	private static final String LOG4J_CONFIG   = "resource/log4j.properties";
	
	public static LogicServerManager connectManager = new LogicServerManager();
	
    public static LogicTcpServer loginTcpServer = new LogicTcpServer();
    
	public static void main(String[] args) throws IOException 
    {
		PropertyConfigurator.configure(LOG4J_CONFIG);
		
		loginTcpServer.Start(connectManager);
    }
}
