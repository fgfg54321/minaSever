package org.apache.mina.logic.chat.codec.client.login;

import org.apache.mina.client.Config;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;

public class ClientLoginReader extends TCPBaseWriter
{
	
    public int GetMessageId()
    {
    	return Config.MESSAGE_LOGIN;
    }
    
    public  void ReadContent(ProtocolStreamReader reader)
    {
    	String token    = reader.ReadString16();
    	String userName = reader.ReadString16();
    	long uid        = reader.ReadInt64();
    	
    }
}
