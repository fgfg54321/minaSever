package org.apache.mina.tcp.base.transserver.protocol.connect;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class LogicConnectWriter extends TCPBaseWriter
{
    public boolean result;
    public int     errorCode;
    public String  message;
    
    @Override
    public int GetSrcServerId()
    {
    	return TServerConfig.SERVER_ID;
    }
    
    @Override
    public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_LOGIN;
    }
    
    @Override
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	writer.WriteBoolean(result);
    	writer.WriteInt32(errorCode);
    	writer.WriteString16(message);
    }
}

