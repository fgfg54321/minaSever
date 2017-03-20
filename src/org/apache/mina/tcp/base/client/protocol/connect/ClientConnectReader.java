package org.apache.mina.tcp.base.client.protocol.connect;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.client.ClientConfig;
import org.apache.mina.tcp.base.client.protocol.custom.LogicLoginWriter;
import org.apache.mina.tcp.base.stream.TCPBaseReader;

public class ClientConnectReader extends TCPBaseReader
{
	public boolean result;
	public int     code;
	public String  message;
	
	public long GetDstServerId()
    {
    	return ClientConfig.CONNECT_SERVER_ID;
    }
	
    public int GetMessageId()
    {
    	return ClientConfig.CONNECT_MESSAGE_LOGIN;
    }
    
    public  void ReadContent(ProtocolStreamReader reader)
    {
    	result     = reader.ReadBoolean();
    	code  = reader.ReadInt32();
    	message    = reader.ReadString16();
    }
    
    public void  OnReader(IoSession session,Object param)
    {
    	LogicLoginWriter login = new LogicLoginWriter();
    	session.write(login);
    }
    
}
