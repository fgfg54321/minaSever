package org.apache.mina.tcp.base.logicserver.protocol.connect;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.logicserver.LogicConfig;
import org.apache.mina.tcp.base.logicserver.protocol.customer.LogicBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectLServer;

public class TServerConnectWriter extends LogicBaseWriter
{
	public TServerConnectWriter(ConnectLServer logicServer)
	{
		this.connectInfo = logicServer;
	}
	
    public int GetMessageId()
    {
    	return LogicConfig.MESSAGE_CONNECT_LOGIN;
    }
    
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	connectInfo.Write(writer);
    }
	
}
