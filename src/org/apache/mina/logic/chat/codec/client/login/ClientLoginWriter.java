package org.apache.mina.logic.chat.codec.client.login;

import org.apache.mina.client.Config;
import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;

public class ClientLoginWriter extends TCPBaseWriter
{
	public String token;
	public String userName;
	public long   uid;
    
    public int GetMessageId()
    {
    	return Config.MESSAGE_LOGIN;
    }
    
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	String result  = "";
    	int errorCode  = 0;
    	String message = "";
    	writer.WriteString16(result);
    	writer.WriteInt32(errorCode);
    	writer.WriteString16(message);
    	
    }
	
}
