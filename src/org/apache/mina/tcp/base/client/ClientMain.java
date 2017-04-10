package org.apache.mina.tcp.base.client;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;

public class ClientMain
{
	private static final String LOG4J_CONFIG   = "resource/log4j.properties";
	
    public static ClientManager clientManager    = new ClientManager();
    public static ClientTcpServer loginTcpServer = new ClientTcpServer();
    
	public static void main(String[] args) throws IOException 
    {
		PropertyConfigurator.configure(LOG4J_CONFIG);
		
		loginTcpServer.Start(clientManager);
    }
}
