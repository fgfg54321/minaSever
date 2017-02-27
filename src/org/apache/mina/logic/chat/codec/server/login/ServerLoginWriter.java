package org.apache.mina.logic.chat.codec.server.login;

import org.apache.mina.logic.chat.Config;
import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectLServer;

public class ServerLoginWriter extends TCPBaseWriter
{
    public ConnectLServer connectServer = new ConnectLServer();
    
    public int GetSrcServerId()
    {
    	return Config.SERVER_ID;
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
    	connectServer.Write(writer);
    }
}

