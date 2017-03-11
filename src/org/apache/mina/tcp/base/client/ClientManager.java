package org.apache.mina.tcp.base.client;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.tcp.base.client.protocol.connect.ClientConnectWriter;
import org.apache.mina.tcp.base.struct.ConnectLServer;

public class ClientManager
{
	public ConcurrentHashMap<Long,ConnectLServer>  connectServerDic = new ConcurrentHashMap<Long,ConnectLServer>();
	
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
	
	public void ServerConnect(ConnectLServer logicServer)
	{
		long id = logicServer.id;
		if(connectServerDic.contains(id))
		{
			OfflineServer(logicServer);
			connectServerDic.replace(id, logicServer);
		}
		else
		{
			connectServerDic.put(id, logicServer);
		}
	}
	
	
	public void OfflineServer(ConnectLServer logicServer)
	{
		
	}
	
}
