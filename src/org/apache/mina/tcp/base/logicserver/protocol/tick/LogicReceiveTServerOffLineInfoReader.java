package org.apache.mina.tcp.base.logicserver.protocol.tick;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.transserver.TServerConfig;
import org.apache.mina.tcp.base.transserver.TransServerManager;

public class LogicReceiveTServerOffLineInfoReader extends TCPBaseReader
{
	
	 /*
     * 0 client 1 server
     */
	 public int type;
	 public long id;
	
    
    public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_OFFLINE;
    }
    
    public  void ReadContent(ProtocolStreamReader reader)
    {
    	type      =  reader.ReadInt32();
    	id        =  reader.ReadInt64();
    }
   
    public void OnReader(IoSession session,Object param)
    {
    	//DO LOGIC LOGOUT
    	
    }
}
