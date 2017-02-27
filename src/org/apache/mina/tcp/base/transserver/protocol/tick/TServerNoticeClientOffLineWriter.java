package org.apache.mina.tcp.base.transserver.protocol.tick;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class TServerNoticeClientOffLineWriter extends TCPBaseWriter
{
	
    public long uid;
    
    @Override
    public int GetSrcServerId()
    {
    	return TServerConfig.SERVER_ID;
    }
    
    @Override
    public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_OFFLINE;
    }
    
    @Override
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	writer.WriteInt64(uid);
    }
}