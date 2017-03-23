package org.apache.mina.tcp.base.transserver.protocol.connect;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectLServer;
import org.apache.mina.tcp.base.struct.ConnectTServer;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class LogicConnectWriter extends TCPBaseWriter
{
    public boolean result    = true;
    public int     code      = 0;
    public String  message   = "success";
    
    public  ConnectLServer logicServer;
    public  ConnectTServer tranServer;
    
    public LogicConnectWriter(ConnectLServer logicServer,ConnectTServer tranServer)
    {
    	this.logicServer = logicServer;
    	this.tranServer  = tranServer;
    }
    
    public long GetDstServerId()
    {
    	return logicServer.id;
    }
    
    @Override
    public long GetSrcServerId()
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
    	tranServer.Write(writer);
    	writer.WriteBoolean(result);
    	writer.WriteInt32(code);
    	writer.WriteString16(message);
    }
}

