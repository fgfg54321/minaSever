package org.apache.mina.tcp.base.transserver.protocol.connect;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.ConnectBase;
import org.apache.mina.tcp.base.struct.ConnectTServer;
import org.apache.mina.tcp.base.transserver.TServerConfig;
import org.apache.mina.tcp.base.transserver.TransServerManager;

public class TServerDSReceiveConnectReader extends TCPBaseReader
{
	
	public String token;
	public String userName;
	public long   serverId;
	
	public ConnectTServer connectServer = new ConnectTServer();
	  
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
    public  void ReadContent(ProtocolStreamReader reader)
    {
    	token    = reader.ReadString16();
    	userName = reader.ReadString16();
    	serverId = reader.ReadInt64();
    	
    	connectServer = (ConnectTServer) ConnectBase.ConnectFactory(reader);
    }
	
	@Override
    public void OnReader(IoSession session,Object param)
    {
    	TransServerManager manager = (TransServerManager)param;
    	manager.TServerLogin(session, connectServer);
    }
    
}
