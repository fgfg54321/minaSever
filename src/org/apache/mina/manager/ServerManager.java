package org.apache.mina.manager;

import java.io.IOException;

import org.apache.mina.login.GateServerTcp;
import org.apache.mina.login.LoginTcpServer;

public class ServerManager
{
	public LoginTcpServer			loginServer;
	public GateServerTcp			gateServer;

	private static ServerManager	instance;

	public static ServerManager getInstance()
	{
		if (instance == null)
		{
			instance = new ServerManager();
		}

		return instance;
	}

	public void Initialize() throws IOException
	{
		loginServer = new LoginTcpServer();
		gateServer  = new GateServerTcp();
	}

	public static void main(String[] args) throws IOException
	{
		ServerManager.getInstance().Initialize();
	}
}
