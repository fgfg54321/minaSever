package org.apache.mina.tcp.base.transserver;

import java.io.IOException;

import org.apache.mina.tcp.base.struct.TransServerManager;

public class TransMain
{
	public static TransServerManager transServerManager = new TransServerManager();
	
    public static TransTcpServerListener  transTcpServerListener  = new TransTcpServerListener();
    public static TransTcpServerConnector transServerConnector    = new TransTcpServerConnector();
	public static void main(String[] args) throws IOException 
    {
		transTcpServerListener.Start(transServerManager);
		transServerConnector.Start(transServerManager);
    }
}
