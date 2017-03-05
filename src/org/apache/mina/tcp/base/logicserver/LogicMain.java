package org.apache.mina.tcp.base.logicserver;

import java.io.IOException;

public class LogicMain
{
	public static LogicServerManager connectManager = new LogicServerManager();
	
    public static LogicTcpServer loginTcpServer = new LogicTcpServer();
    
	public static void main(String[] args) throws IOException 
    {
		loginTcpServer.Start(connectManager);
    }
}
