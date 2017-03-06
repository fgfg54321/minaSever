package org.apache.mina.tcp.base.logicserver.protocol.connect;

import org.apache.mina.client.Config;
import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.logicserver.protocol.customer.LogicBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectLServer;

public class TServerConnectWriter extends LogicBaseWriter
{
    
	public TServerConnectWriter(ConnectLServer logicServer)
	{
		this.connectInfo = logicServer;
	}
	
	public int GetDstServerId()
    {
    	return Config.LOGIN_SERVER;
    }
	
    public int GetMessageId()
    {
    	return Config.MESSAGE_LOGIN;
    }
    
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	connectInfo.Write(writer);
    }
	
}
