package org.apache.mina.tcp.base.struct;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.tcp.base.logicserver.LogicConfig;
import org.apache.mina.tcp.base.logicserver.protocol.customer.client.LogicClientLoginReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;

public class BaseConnectManager
{
	public static final int    INVALID_ID      = -1;
	
	public static final String TYPE_BELONG     = "belong";
	public static final int    TYPE_CLIENT     = 0;//"client";
	public static final int    TYPE_SERVER     = 1;//"server";
	
	public static final String TYPE_LOGSTATUS  = "login";
	public static final int    TYPE_LOGOUT     = 0;
	public static final int    TYPE_LOGIN      = 1;
	
	public static final String TYPE_UID        = "uid";
	public static final String TYPE_SERVERID   = "sId";

	public ConcurrentHashMap<Long,ClientInfo>  clientInfoMap = new ConcurrentHashMap<Long,ClientInfo>();

	public boolean Login(TCPBaseReader tcpRequest,IoSession session)
	{
		if(!IsValid(tcpRequest))
		{
			return false;
		}
		
		ClientInfo clientInfo               = new ClientInfo();
		LogicClientLoginReader logicLoginReader = (LogicClientLoginReader)tcpRequest;
		if(logicLoginReader.GetMessageId() == LogicConfig.MESSAGE_LOGIC_LOGIN)
		{
			long uid         = logicLoginReader.uid;
			String name      = logicLoginReader.userName;
			String token     = logicLoginReader.token;
			clientInfo.uid   = uid;
			clientInfo.name  = name;
			clientInfo.token = token;
			
			if(!clientInfoMap.containsKey(uid))
			{
				clientInfoMap.put(uid, clientInfo);
			}
			else
			{
				ClientInfo oldClientInfo  = clientInfoMap.get(uid);
				oldClientInfo.Logout(session);
				clientInfoMap.replace(uid, clientInfo);
			}
			
			return true;
			
		}
		
		return false;
	}
	
	
	/*
	 * override function start
	 */
	
	public boolean IsValid(TCPBaseReader tcpRequest)
	{
		return true;
	}
	
	protected boolean IsClientLogin(long uid)
	{
		return clientInfoMap.contains(uid);
	}
	
	public void ClientLoginOut(long uid,IoSession session)
	{
		if(clientInfoMap.contains(uid))
		{
			ClientInfo oldClientInfo  = clientInfoMap.get(uid);
			oldClientInfo.Logout(session);
		}
	}
	
	/*
	 * override function end
	 */
	
}
