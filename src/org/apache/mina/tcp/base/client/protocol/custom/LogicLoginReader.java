package org.apache.mina.tcp.base.client.protocol.custom;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.client.ClientConfig;
import org.apache.mina.tcp.base.client.ClientManager;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.ConnectBase;
import org.apache.mina.tcp.base.struct.ConnectLServer;

public class LogicLoginReader extends TCPBaseReader
{
	public ConnectLServer logicServer;
	
	public boolean result;
	public int code;
	public String message;

	public long GetDstServerId()
    {
    	return ClientConfig.LOGIC_SERVER_ID;
    }
	
    public int GetMessageId()
    {
    	return ClientConfig.LOGIC_MESSAGE_LOGIN;
    }
    
    public  void ReadContent(ProtocolStreamReader reader)
    {
    	result      = reader.ReadBoolean();
    	code        = reader.ReadInt32();
    	message     = reader.ReadString16();
    	
    }
    
    public void  OnReader(IoSession session,Object param)
    {
    	ClientManager  clientManager = (ClientManager)param;
    	clientManager.ServerConnect(logicServer);
    }
    
}
