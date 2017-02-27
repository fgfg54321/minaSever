package org.apache.mina.tcp.base.logicserver.protocol.connect;

import org.apache.mina.client.Config;
import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;

public class TServerConnectWriter extends TCPBaseWriter
{
	public String token;
	public String userName;
	public long   uid;
    
	public int GetDstServerId()
    {
    	return Config.LOGIN_SERVER;
    }
	
    public int GetMessageId()
    {
    	return Config.MESSAGE_LOGIN;
    }
    
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	writer.WriteString16(token);
    	writer.WriteString16(userName);
    	writer.WriteInt64(uid);
    }
	
}
