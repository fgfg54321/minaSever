package org.apache.mina.tcp.base.struct;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.tcp.base.logicserver.LogicConfig;
import org.apache.mina.tcp.base.logicserver.protocol.tick.LogicReceiveTServerOffLineInfoWriter;

public class LogicConnectManager
{
	public static final int    INVALID_ID     = -1;
	
	public static final String TYPE_ID        = "id";

	public ConcurrentHashMap<Long,ConnectTServer>  connectTServerDic = new ConcurrentHashMap<Long,ConnectTServer>();
	public ConcurrentHashMap<Long,LogicServerInfo> lServerInfoDic    = new ConcurrentHashMap<Long,LogicServerInfo>();
	public ConcurrentHashMap<Long,ClientInfo>      clientInfoDic     = new ConcurrentHashMap<Long,ClientInfo>();
	
	
	public void ClientLogin(long uid)
	{
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.uid        = uid;
		if(!clientInfoDic.containsKey(uid))
		{
			clientInfoDic.put(uid, clientInfo);
		}
		else
		{
			ClientTick(uid);
			clientInfoDic.replace(uid, clientInfo);
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
	
	protected void ClientTick(long uid)
	{
		ClientInfo clientInfo = clientInfoDic.get(uid);
		clientInfo.uid        = uid;
		
		int route              = clientInfo.route;
		ConnectTServer tServer = connectTServerDic.get(route);
		IoSession session      = tServer.session;
		
		LogicReceiveTServerOffLineInfoWriter offLineInfo = new LogicReceiveTServerOffLineInfoWriter();
		offLineInfo.id        = uid;
		offLineInfo.type      = LogicConfig.TYPE_CLIENT;
		session.write(offLineInfo);
	}
	
	public void LogicServerLogin(long serverId)
	{
		LogicServerInfo serverInfo = new LogicServerInfo();
		serverInfo.serverId        = serverId;
		lServerInfoDic.put(serverId, serverInfo);
	}
	
	public void LogicServerLoginOut(long serverId)
	{
		LogicServerInfo serverInfo = new LogicServerInfo();
		serverInfo.serverId   = serverId;
		lServerInfoDic.put(serverId, serverInfo);
	}
	
	protected void LogicServerTick(long serverId)
	{
		LogicServerInfo serverInfo = lServerInfoDic.get(serverId);
		int routeId                = serverInfo.route;
		ConnectTServer tServer     = connectTServerDic.get(routeId);
		IoSession session          = tServer.session;
		LogicReceiveTServerOffLineInfoWriter offLineInfo = new LogicReceiveTServerOffLineInfoWriter();
		offLineInfo.id             = serverId;
		offLineInfo.type           = LogicConfig.TYPE_LSERVER;
		session.write(offLineInfo);
	}
	
	
	public void ConnectTServer(IoSession session,long serverId)
	{
		ConnectTServer server = new ConnectTServer();
		server.session        = session;
		session.setAttribute(TYPE_ID, serverId);
		if(!connectTServerDic.contains(serverId))
		{
			connectTServerDic.put(serverId, server);	
		}
		else
		{
			TServerDisconnect(serverId);
			connectTServerDic.replace(serverId, server);
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
	
	
}
