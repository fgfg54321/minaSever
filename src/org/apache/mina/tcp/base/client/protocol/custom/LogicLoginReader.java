package org.apache.mina.tcp.base.client.protocol.custom;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.client.ClientConfig;
import org.apache.mina.tcp.base.stream.TCPBaseReader;

public class LogicLoginReader extends TCPBaseReader
{

	public int GetDstServerId()
    {
    	return ClientConfig.LOGIC_SERVER_ID;
    }
	
    public int GetMessageId()
    {
    	return ClientConfig.LOGIC_MESSAGE_LOGIN;
    }
    
    public  void ReadContent(ProtocolStreamReader reader)
    {
    	String result  = reader.ReadString16();
    	int errorCode  = reader.ReadInt32();
    	String message = reader.ReadString16();
    }
    
    public void  OnReader(IoSession session,Object param)
    {
    	
    }
    
}
