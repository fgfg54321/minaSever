package org.apache.mina.manager;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.tcp.base.transserver.TransTcpServerListener;

public class ServerManager
{
	public TransTcpServerListener			loginServer;

	public ConcurrentHashMap<Long,IoSession> sessionMap = new ConcurrentHashMap<Long,IoSession>();
			
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
		loginServer = new TransTcpServerListener();
	}

	public static void main(String[] args) throws IOException
	{
		ServerManager.getInstance().Initialize();
	}
}
