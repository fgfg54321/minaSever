package org.apache.mina.tcp.base.logicserver.protocol.tick;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectLServer;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class LogicReceiveTServerOffLineInfoWriter extends TCPBaseWriter
{
	
	public ConnectLServer logicServer;
	 /*
     * 0 client 1 server
     */ 
	public int type;
	public long id;
	 
	public LogicReceiveTServerOffLineInfoWriter(ConnectLServer logicServer)
	{
		this.logicServer = logicServer;
	}
    
    public long GetSrcServerId()
    {
    	return TServerConfig.SERVER_ID;
    }
    
    public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_OFFLINE;
    }
    
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	logicServer.Write(writer);
    	writer.WriteInt32(type);
    	writer.WriteInt64(id);
    }
	 
}