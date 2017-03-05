package org.apache.mina.tcp.base.logicserver.protocol.connect;

import org.apache.mina.client.Config;
import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectLServer;

public class TServerConnectWriter extends TCPBaseWriter
{
	public ConnectLServer logicServer;
    
	public TServerConnectWriter(ConnectLServer logicServer)
	{
		this.logicServer = logicServer;
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
    	logicServer.Write(writer);
    }
	
}
