package org.apache.mina.tcp.base.logicserver;

import java.io.IOException;

import org.apache.mina.tcp.base.struct.LogicConnectManager;

public class LogicMain
{
	public static LogicConnectManager connectManager;
	
    public static LogicTcpServer loginTcpServer = new LogicTcpServer();
    
	public static void main(String[] args) throws IOException 
    {
		loginTcpServer.Start(connectManager);
    }
}
