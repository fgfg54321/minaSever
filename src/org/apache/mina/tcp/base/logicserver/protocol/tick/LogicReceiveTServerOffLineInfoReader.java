package org.apache.mina.tcp.base.logicserver.protocol.tick;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.logicserver.LogicConfig;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.ConnectBase;
import org.apache.mina.tcp.base.struct.ConnectTServer;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class LogicReceiveTServerOffLineInfoReader extends TCPBaseReader
{
	
	 /*
     * 0 client 1 server
     */
    
     public ConnectTServer tserver;
	 public int type;
	 public long id;
	
    public LogicReceiveTServerOffLineInfoReader(ConnectTServer tserver)
    {
    	this.tserver = tserver;
    }
	 
    public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_OFFLINE;
    }
    
    public  void ReadContent(ProtocolStreamReader reader)
    {
    	tserver   =  (ConnectTServer)ConnectBase.ConnectFactory(reader);
    	type      =  reader.ReadInt32();
    	id        =  reader.ReadInt64();
    }
   
    public void OnReader(IoSession session,Object param)
    {
    	//DO LOGIC LOGOUT
    	if(type == LogicConfig.TYPE_CLIENT)
    	{
    		
    	}
    	else if(type == LogicConfig.TYPE_LSERVER)
    	{
    		
    	}
    }
}
