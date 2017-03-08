package org.apache.mina.tcp.base.client.protocol.connect;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.client.ClientConfig;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;

public class ClientConnectWriter extends TCPBaseWriter
{
	public String token;
	public String userName;
	public long   uid;
    
	public long GetDstServerId()
    {
    	return ClientConfig.CONNECT_SERVER_ID;
    }
	
    public int GetMessageId()
    {
    	return ClientConfig.CONNECT_MESSAGE_LOGIN;
    }
    
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	writer.WriteString16(token);
    	writer.WriteString16(userName);
    	writer.WriteInt64(uid);
    }
}

