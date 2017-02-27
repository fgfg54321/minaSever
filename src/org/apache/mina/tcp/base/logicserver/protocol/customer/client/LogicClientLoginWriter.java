package org.apache.mina.tcp.base.logicserver.protocol.customer.client;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.logicserver.LogicConfig;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;

public class LogicClientLoginWriter extends TCPBaseWriter
{
	public String token;
	public String userName;
	public long   uid;
    
	public int GetDstServerId()
    {
    	return LogicConfig.SERVER_ID;
    }
	
    public int GetMessageId()
    {
    	return LogicConfig.MESSAGE_LOGIC_LOGIN;
    }
    
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	writer.WriteString16(result);
    	writer.WriteInt32(errorCode);
    	writer.WriteInt64(message);
    }
}

