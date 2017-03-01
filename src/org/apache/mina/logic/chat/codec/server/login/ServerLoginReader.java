package org.apache.mina.logic.chat.codec.server.login;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.ConnectLServer;
import org.apache.mina.tcp.base.transserver.TServerConfig;
import org.apache.mina.tcp.base.transserver.TransServerManager;

public class ServerLoginReader extends TCPBaseReader
{
	public ConnectLServer connectServer = new ConnectLServer();
    
	public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_LOGIN;
    }
    
    public  void ReadContent(ProtocolStreamReader reader)
    {
    	connectServer.Read(reader);
    }
    
    public void OnReader(IoSession session,Object param)
    {
    	TransServerManager manager = (TransServerManager)param;
    	manager.Login(session, this);
    }
    
}
