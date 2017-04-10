package org.apache.mina.tcp.base.transserver;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;

public class TransMain
{
	private static final String LOG4J_CONFIG   = "resource/log4j.properties";
	
	public static TransServerManager transServerManager = new TransServerManager();
	
    public static TransTcpServerListener  transTcpServerListener  = new TransTcpServerListener();
    public static TransTcpServerConnector transServerConnector    = new TransTcpServerConnector();
	public static void main(String[] args) throws IOException 
    {
		PropertyConfigurator.configure(LOG4J_CONFIG);
		
		transTcpServerListener.Start(transServerManager);
		//transServerConnector.Start(transServerManager);
    }
}
