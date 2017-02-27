package org.apache.mina.tcp.base.transserver.protocol.connect;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.ConnectTServer;
import org.apache.mina.tcp.base.struct.TransServerManager;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class TServerDSSendConnectReader extends TCPBaseReader
{

    public boolean result;
    public int     errorCode;
    public String  message;
    

	public ConnectTServer connectServer = new ConnectTServer();
	  
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
    public  void ReadContent(ProtocolStreamReader reader)
    {
		 result     = reader.ReadBoolean();
    	 errorCode  = reader.ReadInt32();
    	 message    = reader.ReadString16();
    }
	
	@Override
    public void OnReader(IoSession session,Object param)
    {
    	TransServerManager manager = (TransServerManager)param;
    	manager.Login(session, this);
    }
    
}
