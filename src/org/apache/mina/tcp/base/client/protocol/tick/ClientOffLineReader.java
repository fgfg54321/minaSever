package org.apache.mina.tcp.base.client.protocol.tick;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.client.ClientConfig;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class ClientOffLineReader extends TCPBaseReader
{
	public long uid;
	
	public int GetSrcServerId()
    {
    	return ClientConfig.CONNECT_SERVER_ID;
    }
	 
    public int GetMessageId()
    {
    	return ClientConfig.CONNECT_MESSAGE_OFFLINE;
    }
    
    public  void ReadContent(ProtocolStreamReader reader)
    {
    	uid        =  reader.ReadInt64();
    }
   
    public void OnReader(IoSession session,Object param)
    {
    	//DO CLIENT LOGOUT
    	
    }
}
