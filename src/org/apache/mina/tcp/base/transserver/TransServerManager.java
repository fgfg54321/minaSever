package org.apache.mina.tcp.base.transserver;

import java.net.InetSocketAddress;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.ConnectClient;
import org.apache.mina.tcp.base.struct.ConnectLServer;
import org.apache.mina.tcp.base.struct.ConnectTServer;
import org.apache.mina.tcp.base.transserver.protocol.connect.ClientConnectReader;
import org.apache.mina.tcp.base.transserver.protocol.connect.ClientConnectWriter;
import org.apache.mina.tcp.base.transserver.protocol.connect.LogicConnectReader;
import org.apache.mina.tcp.base.transserver.protocol.connect.LogicConnectWriter;
import org.apache.mina.tcp.base.transserver.protocol.connect.TServerDSSendConnectReader;
import org.apache.mina.tcp.base.transserver.protocol.tick.TServerNoticeClientOffLineWriter;
import org.apache.mina.tcp.base.transserver.protocol.tick.TServerNoticeTServerDsOffLineWriter;
import org.apache.mina.tcp.base.transserver.protocol.tick.TServerNoticeLogicOffLineInfoWriter;

public class TransServerManager
{
	public static final int    INVALID_ID      = -1;
	
	public static final String TYPE_BELONG     = "belong";
	public static final int    TYPE_CLIENT     = 0;//"client";
	public static final int    TYPE_LSERVER    = 1;//"logic server";
	public static final int    TYPE_TSERVER    = 2;//"trans server";
	
	public static final String TYPE_LOGSTATUS  = "login";
	public static final int    TYPE_LOGOUT     = 0;
	public static final int    TYPE_LOGIN      = 1;
	
	public static final String TYPE_ID         = "id";

	public ConcurrentHashMap<Long,ConnectClient>   clientConnectMap      = new ConcurrentHashMap<Long,ConnectClient>();
	
	public ConcurrentHashMap<Long,ConnectLServer>  logicServerConnectMap = new ConcurrentHashMap<Long,ConnectLServer>();
	
	public ConcurrentHashMap<Long,ConnectTServer>  transServerConnectMap = new ConcurrentHashMap<Long,ConnectTServer>();
	
	public ConnectTServer transTServerConnect;
	
	public boolean Login(IoSession session,TCPBaseReader tcpRequest)
	{
		if(tcpRequest.GetMessageId() != TServerConfig.MESSAGE_LOGIN)
		{
			session.close(true);
			return false;
		}
		
    	if(IsTServer(session))
    	{
    		TServerDSSendConnectReader serverLogin = (TServerDSSendConnectReader)tcpRequest;
			ConnectTServer server              = serverLogin.connectServer;
			session.setAttribute(TYPE_LOGSTATUS, TYPE_LOGIN);
			long srcServerId                   =  server.id;
			session.setAttribute(TYPE_ID, srcServerId);
			if(transServerConnectMap.containsKey(srcServerId))
			{
				LServerTick(srcServerId);
				transServerConnectMap.replace(srcServerId, server);
			}
			else
			{
				LogicConnectWriter loginResponse = new LogicConnectWriter();
				session.write(loginResponse);
				transServerConnectMap.put(srcServerId, server);
			}
			
			return true;
    	}
    	else if(IsLServer(session))
    	{
    		LogicConnectReader serverLogin = (LogicConnectReader)tcpRequest;
			ConnectLServer server           = serverLogin.connectServer;
			session.setAttribute(TYPE_LOGSTATUS, TYPE_LOGIN);
			long srcServerId               =  server.id;
			session.setAttribute(TYPE_ID, srcServerId);
			if(logicServerConnectMap.containsKey(srcServerId))
			{
				LServerTick(srcServerId);
				logicServerConnectMap.replace(srcServerId, server);
			}
			else
			{
				LogicConnectWriter loginResponse = new LogicConnectWriter();
				session.write(loginResponse);
				logicServerConnectMap.put(srcServerId, server);
			}
			
			return true;
    	}
    	else if(IsClient(session))
    	{
    		ClientConnectReader clientLogin    = (ClientConnectReader)tcpRequest;
    		ConnectClient client                 = clientLogin.connectClient;
    		session.setAttribute(TYPE_LOGSTATUS, TYPE_LOGIN);
    		long uid                       = client.id;
    		session.setAttribute(TYPE_ID, uid);
    		
    		if(clientConnectMap.containsKey(uid))
    		{
    			ClientTick(uid);
    			clientConnectMap.replace(uid, client);
    		}
    		else
    		{
    			ClientConnectWriter loginResponse = new ClientConnectWriter();
				session.write(loginResponse);
    			clientConnectMap.put(uid, client);
    		}
    		
    		return true;
    	}
    	return false;
	}
	
	
	public void ClientLoginOut(long uid)
	{
		if(clientConnectMap.containsKey(uid))
		{
			ClientTick(uid);
			clientConnectMap.remove(uid);
		}
	}
	
	public void LServerLoginOut(long serverId)
	{
		if(logicServerConnectMap.containsKey(serverId))
		{
			LServerTick(serverId);
			logicServerConnectMap.remove(serverId);
		}
	}
	
	public void TServerLoginOut(long serverId)
	{
		if(transServerConnectMap.containsKey(serverId))
		{
			TServerTick(serverId);
			transServerConnectMap.remove(serverId);
		}
	}
	
	public void LoginOut(IoSession session)
	{
		if(!IsLogin(session))
		{
			return;
		}
		
		if(IsLServer(session))
    	{
			long serverId = (Long)session.getAttribute(TYPE_ID);
			LServerTick(serverId);
			logicServerConnectMap.remove(serverId);
    	}
		else if(IsClient(session))
		{
			long uid = (Long)session.getAttribute(TYPE_ID);
			ClientTick(uid);
			clientConnectMap.remove(uid);
		}
		else if(IsTServer(session))
		{
			long serverId = (Long)session.getAttribute(TYPE_ID);
			TServerTick(serverId);
			transServerConnectMap.remove(serverId);
		}
	}
	
	public boolean SetConnectBelong(IoSession session)
	{
		InetSocketAddress socketAdress = (InetSocketAddress)session.getLocalAddress();
    	int port                       = socketAdress.getPort();
    	if(port == TServerConfig.LSEVER_PORT)
    	{
			session.setAttribute(TYPE_BELONG, TYPE_LSERVER);
			
			return true;
    	}
    	else if(port == TServerConfig.CLIENT_PORT)
    	{
    		session.setAttribute(TYPE_BELONG, TYPE_CLIENT);
    		
    		return true;
    	}
    	else if(port == TServerConfig.TSEVER_PORT)
    	{
    		session.setAttribute(TYPE_BELONG, TYPE_TSERVER);
    		
    		return true;
    	}
    	return false;
	}
	
	
	public void SetTransServerConnector(int serverId,String serverName,IoSession session)
	{
		
		ConnectTServer tServer = new ConnectTServer();
		tServer.id       = serverId;
		tServer.name     = serverName;
		tServer.session        = session;
	}
	

	public ConnectTServer GetTransServerConnector()
	{
		return transTServerConnect;
	}
	
	
	protected void LServerTick(long serverId)
	{
		ConnectLServer oldServer             = logicServerConnectMap.get(serverId);
		IoSession oldSession                = oldServer.session;
		TServerNoticeLogicOffLineInfoWriter offLineResponse = new TServerNoticeLogicOffLineInfoWriter();
		offLineResponse.SetType(TServerConfig.TYPE_LSERVER,serverId);
		oldSession.write(offLineResponse);
		oldSession.closeOnFlush();
	}
	

	protected void TServerTick(long serverId)
	{
		ConnectLServer oldServer            = logicServerConnectMap.get(serverId);
		IoSession oldSession                = oldServer.session;
		TServerNoticeTServerDsOffLineWriter offLineResponse = new TServerNoticeTServerDsOffLineWriter();
		oldSession.write(offLineResponse);
		oldSession.closeOnFlush();
	}
	
	protected void ClientTick(long uid)
	{
		ConnectClient oldClient             = clientConnectMap.get(uid);
		IoSession oldSession                = oldClient.session;
		TServerNoticeClientOffLineWriter offLineResponse = new TServerNoticeClientOffLineWriter();
		oldSession.write(offLineResponse);
		
		TServerNoticeLogicOffLineInfoWriter offLineResponseNoticeServer = new TServerNoticeLogicOffLineInfoWriter();
		offLineResponseNoticeServer.SetType(TServerConfig.TYPE_CLIENT,uid);
		oldSession.write(offLineResponseNoticeServer);
		
		oldSession.closeOnFlush();
	}
	
	
	
	public ConnectLServer GetLServer(IoSession session)
	{
		long serverId = (Long) session.getAttribute(TYPE_ID, INVALID_ID);
		if(logicServerConnectMap.containsKey(serverId))
		{
			return logicServerConnectMap.get(serverId);
		}
		return null;
	}
	
	public ConnectLServer GetLServer(long serverId)
	{
		if(logicServerConnectMap.containsKey(serverId))
		{
			return logicServerConnectMap.get(serverId);
		}
		return null;
	}
	
	public ConnectTServer GetTServer(IoSession session)
	{
		long serverId = (Long) session.getAttribute(TYPE_ID, INVALID_ID);
		if(transServerConnectMap.containsKey(serverId))
		{
			return transServerConnectMap.get(serverId);
		}
		return null;
	}
	
	public ConnectTServer GetTServer(long serverId)
	{
		if(transServerConnectMap.containsKey(serverId))
		{
			return transServerConnectMap.get(serverId);
		}
		return null;
	}
	
	public ConnectClient GetClient(IoSession session)
	{
		long uid = (Long) session.getAttribute(TYPE_ID,INVALID_ID);
		if(clientConnectMap.containsKey(uid))
		{
			return clientConnectMap.get(uid);
		}
		return null;
	}
	
	public ConnectClient GetClient(long uid)
	{
		if(clientConnectMap.containsKey(uid))
		{
			return clientConnectMap.get(uid);
		}
		return null;
	}
	
	public static boolean IsLogin(IoSession session)
	{
		Object obj = session.getAttribute(TYPE_LOGSTATUS);
		return obj != null && obj.equals(TYPE_LOGIN);
	}
	
	public static boolean IsClient(IoSession session)
	{
		Object obj = session.getAttribute(TYPE_BELONG);
		return obj != null && obj.equals(TYPE_CLIENT);
	}
	
	public static boolean IsTServer(IoSession session)
	{
		Object obj = session.getAttribute(TYPE_BELONG);
		return obj != null && obj.equals(TYPE_TSERVER);
	}
	
	public static boolean IsLServer(IoSession session)
	{
		Object obj = session.getAttribute(TYPE_BELONG);
		return obj != null && obj.equals(TYPE_LSERVER);
	}
	
	public void Remove(IoSession session)
	{
		if(IsClient(session))
		{
			Enumeration<ConnectClient> etr = clientConnectMap.elements();
			  while(etr.hasMoreElements())
			  {
				ConnectClient client = etr.nextElement();
				if(client.session == session)
				{
					clientConnectMap.remove(client.id);
				}
			  }
		}
		else if(IsLServer(session))
		{
		  Enumeration<ConnectLServer> etr = logicServerConnectMap.elements();
		  while(etr.hasMoreElements())
		  {
			ConnectLServer server = etr.nextElement();
			if(server.session == session)
			{
				logicServerConnectMap.remove(server.id);
			}
		  }
		}
		else if(IsTServer(session))
		{
		  Enumeration<ConnectTServer> etr = transServerConnectMap.elements();
		  while(etr.hasMoreElements())
		  {
			ConnectTServer server = etr.nextElement();
			if(server.session == session)
			{
				transServerConnectMap.remove(server.id);
			}
		  }
		}
	}
	
}
