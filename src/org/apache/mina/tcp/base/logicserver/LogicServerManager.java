package org.apache.mina.tcp.base.logicserver;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.tcp.base.logicserver.protocol.connect.TServerConnectWriter;
import org.apache.mina.tcp.base.logicserver.protocol.customer.LogicBaseWriter;
import org.apache.mina.tcp.base.logicserver.protocol.tick.LogicReceiveTServerOffLineInfoWriter;
import org.apache.mina.tcp.base.struct.ConnectClient;
import org.apache.mina.tcp.base.struct.ConnectLServer;
import org.apache.mina.tcp.base.struct.ConnectTServer;
import org.apache.mina.tcp.base.struct.Route;

public class LogicServerManager
{
	public static final int    INVALID_ID     = -1;
	
	public static final String TYPE_ID        = "id";

	public ConcurrentHashMap<Long,ConnectTServer>  connectTServerDic = new ConcurrentHashMap<Long,ConnectTServer>();
	public ConcurrentHashMap<Long,ConnectLServer>  lServerInfoDic    = new ConcurrentHashMap<Long,ConnectLServer>();
	public ConcurrentHashMap<Long,ConnectClient>   clientInfoDic     = new ConcurrentHashMap<Long,ConnectClient>();
	
	public ConnectLServer self = new ConnectLServer();
	
	public LogicServerManager()
	{
		self.id    = LogicConfig.SERVER_ID;
		self.name  = "L";
		self.token = "ll";
	}
	
	public void ClientLogin(ConnectClient clientInfo)
	{
		long id = clientInfo.id;
		if(!clientInfoDic.containsKey(id))
		{
			clientInfoDic.put(id, clientInfo);
		}
		else
		{
			ClientTick(id);
			clientInfoDic.replace(id, clientInfo);
		}
	}

	public void ClientLoginOut(long uid)
	{
		if(clientInfoDic.containsKey(uid))
		{
			ClientTick(uid);
			clientInfoDic.remove(uid);
		}
	}
	
	protected void ClientTick(long id)
	{
		ConnectClient clientInfo = clientInfoDic.get(id);
		clientInfo.id            = id;
		
		Route route              = clientInfo.fromRoute;
		long routeId             = route.id;
		ConnectTServer tServer   = connectTServerDic.get(routeId);
		IoSession session        = tServer.session;
		
		LogicReceiveTServerOffLineInfoWriter offLineInfo = new LogicReceiveTServerOffLineInfoWriter();
		offLineInfo.id        = id;
		offLineInfo.type      = LogicConfig.TYPE_CLIENT;
		session.write(offLineInfo);
	}
	
	public void LogicServerLogin(ConnectLServer serverInfo)
	{
		long id = serverInfo.id;
		if(!lServerInfoDic.containsKey(id))
		{
			lServerInfoDic.put(id, serverInfo);
		}
		else
		{
			LogicServerTick(id);
			lServerInfoDic.put(id, serverInfo);
		}
	}
	
	public void LogicServerLoginOut(long id)
	{
		if(lServerInfoDic.containsKey(id))
		{
			LogicServerTick(id);
			lServerInfoDic.remove(id);
		}
	}
	
	protected void LogicServerTick(long serverId)
	{
		ConnectLServer serverInfo  = lServerInfoDic.get(serverId);
		Route route                = serverInfo.fromRoute;
		long routeId               = route.id;
		ConnectTServer tServer     = connectTServerDic.get(routeId);
		IoSession session          = tServer.session;
		LogicReceiveTServerOffLineInfoWriter offLineInfo = new LogicReceiveTServerOffLineInfoWriter();
		offLineInfo.id             = serverId;
		offLineInfo.type           = LogicConfig.TYPE_LSERVER;
		session.write(offLineInfo);
	}
	

	public void SendConnectRequest(IoSession session)
	{
		TServerConnectWriter connectWriter = new TServerConnectWriter(self);
		session.write(connectWriter);
	}
	
	public void ConnectTServer(IoSession session,ConnectTServer tServer)
	{
		long id                = tServer.id;
		tServer.session        = session;
		session.setAttribute(TYPE_ID, id);
		if(!connectTServerDic.contains(id))
		{
			connectTServerDic.put(id, tServer);	
		}
		else
		{
			TServerDisconnect(id);
			connectTServerDic.replace(id, tServer);
		}
	}
	
	public void TServerTick(IoSession session)
	{
		if(session.containsAttribute(TYPE_ID))
		{
			long serverId = (long)session.getAttribute(TYPE_ID);
			if(connectTServerDic.contains(serverId))
			{
				TServerDisconnect(serverId);
				connectTServerDic.remove(serverId);
			}
		}
	}
	
	protected void TServerDisconnect(long serverId)
	{
		ConnectTServer oldServer             = connectTServerDic.get(serverId);
		IoSession oldSession                 = oldServer.session;
		LogicReceiveTServerOffLineInfoWriter offLineInfo = new LogicReceiveTServerOffLineInfoWriter();
		offLineInfo.id   = serverId;
		offLineInfo.type = LogicConfig.TYPE_TSERVER;
		oldSession.write(offLineInfo);
		oldSession.closeOnFlush();
	}
	
	
	public void SendToClient(long id,LogicBaseWriter logicWriter)
	{
		if(clientInfoDic.containsKey(id))
		{
			ConnectClient client     = clientInfoDic.get(id);
			Route route              = client.fromRoute;
			long routeId             = route.id;
			ConnectTServer tServer   = connectTServerDic.get(routeId);
			IoSession session        = tServer.session;
			
			logicWriter.SetConnectInfo(client);
			session.write(logicWriter);
		}
	}
	
}
