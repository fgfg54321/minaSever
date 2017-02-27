package org.apache.mina.tcp.base.logicserver.protocol.connect;

import org.apache.mina.client.Config;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;

public class TServerConnectReader extends TCPBaseReader
{
	
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
    	String result  = reader.ReadString16();
    	int errorCode  = reader.ReadInt32();
    	String message = reader.ReadString16();
    }
}

