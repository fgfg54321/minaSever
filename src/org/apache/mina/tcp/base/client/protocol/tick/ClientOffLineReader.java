package org.apache.mina.tcp.base.client.protocol.tick;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.client.ClientConfig;
import org.apache.mina.tcp.base.stream.TCPBaseReader;

public class ClientOffLineReader extends TCPBaseReader
{
	public boolean result;
	
	public long GetSrcServerId()
    {
    	return ClientConfig.CONNECT_SERVER_ID;
    }
	 
    public int GetMessageId()
    {
    	return ClientConfig.CONNECT_MESSAGE_OFFLINE;
    }
    
    public  void ReadContent(ProtocolStreamReader reader)
    {
    	result        =  reader.ReadBoolean();
    }
   
    public void OnReader(IoSession session,Object param)
    {
    	//DO CLIENT LOGOUT
    	
    }
}
