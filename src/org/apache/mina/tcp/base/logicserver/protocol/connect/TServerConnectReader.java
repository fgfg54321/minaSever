package org.apache.mina.tcp.base.logicserver.protocol.connect;

import org.apache.mina.client.Config;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.logicserver.LogicServerManager;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.ConnectTServer;

public class TServerConnectReader extends TCPBaseReader
{
	public boolean result;
    public int     errorCode;
    public String  message;
    
	public ConnectTServer tranServer = new ConnectTServer();

	public int GetDstServerId()
    {
    	return Config.LOGIN_SERVER;
    }
	
	public int GetMessageId()
    {
    	return Config.MESSAGE_LOGIN;
    }
    
    public  void ReadContent(ProtocolStreamReader reader)
    {
    	tranServer.Read(reader);
    }
    
    public void  OnReader(IoSession session,Object param)
    {
    	LogicServerManager manager = (LogicServerManager)param;
    	manager.ConnectTServer(session,tranServer);
    }
}

