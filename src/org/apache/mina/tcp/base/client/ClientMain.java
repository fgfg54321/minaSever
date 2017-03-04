package org.apache.mina.tcp.base.client;

import java.io.IOException;

public class ClientMain
{

    public static ClientManager clientManager    = new ClientManager();
    public static ClientTcpServer loginTcpServer = new ClientTcpServer();
    
	public static void main(String[] args) throws IOException 
    {
		loginTcpServer.Start(clientManager);
    }
}
