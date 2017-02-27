package org.apache.mina.tcp.base.client.protocol.tick;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.client.ClientConfig;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class ClientOffLineWriter extends TCPBaseWriter
{
    
    public int GetSrcServerId()
    {
    	return ClientConfig.CONNECT_SERVER_ID;
    }
    
    public int GetMessageId()
    {
    	return ClientConfig.CONNECT_MESSAGE_OFFLINE;
    }
    
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	
    }
	 
}