package org.apache.mina.tcp.base.client;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.tcp.base.client.protocol.connect.ClientConnectWriter;
import org.apache.mina.tcp.base.struct.ConnectTServer;

public class ClientManager
{
	public ConcurrentHashMap<Long,ConnectTServer>  connectTServerDic = new ConcurrentHashMap<Long,ConnectTServer>();
	
	public IoSession session;
	
	public void SetSession(IoSession session)
	{
		this.session = session;
	}
	
	public void ConnectTransServer()
	{
		ClientConnectWriter connectWriter = new ClientConnectWriter();
		session.write(connectWriter);
	}
	
	
	public void ConnectLogicServer()
	{
		
	}
}
