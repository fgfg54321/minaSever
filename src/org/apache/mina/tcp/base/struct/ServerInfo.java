package org.apache.mina.tcp.base.struct;

import org.apache.mina.core.session.IoSession;

public class ServerInfo
{
	public long    serverId;
	public String  serverName;
	public String  token;
	public Integer route;
	
	public void Logout(IoSession session)
	{
		
	}
	
	public void DisConnect(IoSession session)
	{
		
	}

}
