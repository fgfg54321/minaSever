package org.apache.mina.tcp.base.transserver.protocol.connect;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.ConnectClient;
import org.apache.mina.tcp.base.transserver.TServerConfig;
import org.apache.mina.tcp.base.transserver.TransServerManager;

public class ClientConnectReader extends TCPBaseReader
{
	
	public String token;
	public String userName;
	public long   uid;
	
	public ConnectClient connectClient = new ConnectClient();
    
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
    	uid      = reader.ReadInt64();
    }
	
	@Override
    public void OnReader(IoSession session,Object param)
    {
    	TransServerManager manager = (TransServerManager)param;
    	manager.Login(session, this);
    }
    
}
