package org.apache.mina.logic.chat.codec.server.tick;

import org.apache.mina.logic.chat.Config;
import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectLServer;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class ServerOffLineWriter extends TCPBaseWriter
{
	public ConnectLServer connectServer = new ConnectLServer();
    
   
    public int GetSrcServerId()
    {
    	return Config.SERVER_ID;
    }
    
    public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_OFFLINE;
    }
    
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	connectServer.Write(writer);
    }
}