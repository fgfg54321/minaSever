package org.apache.mina.tcp.base.logicserver.protocol.customer.client.login;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.logicserver.LogicConfig;
import org.apache.mina.tcp.base.logicserver.protocol.customer.LogicBaseWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;

public class LogicClientLoginWriter extends LogicBaseWriter
{
	public boolean result    = true;
    public int     code      = 0;
    public String  message   = "success";
    
	public long GetDstServerId()
    {
    	return LogicConfig.SERVER_ID;
    }
	
    public int GetMessageId()
    {
    	return LogicConfig.MESSAGE_LOGIC_LOGIN;
    }
    
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	writer.WriteBoolean(result);
    	writer.WriteInt32(code);
    	writer.WriteString16(message);
    }
}

