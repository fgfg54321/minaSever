package org.apache.mina.tcp.base.transserver.protocol.connect;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.ConnectLServer;
import org.apache.mina.tcp.base.transserver.TServerConfig;
import org.apache.mina.tcp.base.transserver.TransServerManager;

public class LogicConnectReader extends TCPBaseReader
{
	
	public ConnectLServer logicServer = new ConnectLServer();
  
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
		logicServer.Read(reader);
    }
    
	@Override
    public void OnReader(IoSession session,Object param)
    {
    	TransServerManager manager = (TransServerManager)param;
    	manager.LServerLogin(session, logicServer);
    }
    
}
