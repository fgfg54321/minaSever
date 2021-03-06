package org.apache.mina.tcp.base.client.protocol.custom;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.client.ClientConfig;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;

public class LogicLoginWriter extends TCPBaseWriter
{
	public String token    = "sdsssssssssssss";
	public long   uid      = 213;
    
	public long GetDstServerId()
    {
    	return ClientConfig.LOGIC_SERVER_ID;
    }
	
    public int GetMessageId()
    {
    	return ClientConfig.LOGIC_MESSAGE_LOGIN;
    }
    
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	writer.WriteString16(token);
    	writer.WriteInt64(uid);
    }
}

