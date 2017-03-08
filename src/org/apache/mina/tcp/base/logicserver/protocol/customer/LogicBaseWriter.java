package org.apache.mina.tcp.base.logicserver.protocol.customer;

import org.apache.mina.tcp.base.logicserver.LogicConfig;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectBase;
import org.apache.mina.tcp.base.struct.ConnectTServer;

public class LogicBaseWriter extends TCPBaseWriter
{
	protected ConnectTServer transServer;
	
	protected ConnectBase connectInfo;
	
	public ConnectBase GetConnectInfo()
	{
		return connectInfo;
	}
	
	public void SetConnectInfo(ConnectBase connectBase)
	{
		this.connectInfo = connectBase;
	}

	public ConnectTServer getTransServer()
	{
		return transServer;
	}

	public void setTransServer(ConnectTServer transServer)
	{
		this.transServer = transServer;
	}
	
	public long GetSrcServerId()
    {
    	return LogicConfig.SERVER_ID;
    }
	
	public long GetDstServerId()
    {
    	return transServer.id;
    }
	
}
