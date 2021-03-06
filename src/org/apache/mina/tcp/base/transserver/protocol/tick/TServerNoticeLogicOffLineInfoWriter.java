package org.apache.mina.tcp.base.transserver.protocol.tick;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectTServer;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class TServerNoticeLogicOffLineInfoWriter extends TCPBaseWriter
{
	
	public ConnectTServer transServer;
    public int     type;
    public long    id;
    
    public TServerNoticeLogicOffLineInfoWriter(ConnectTServer transServer)
    {
    	this.transServer = transServer;
    }
    
    /*
     * 0 client 1 server
     */
    public void SetType(int type,long id)
    {
    	this.type = type;
    	this.id   = id;
    }
    
    @Override
    public long GetSrcServerId()
    {
    	return TServerConfig.SERVER_ID;
    }
    
    @Override
    public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_OFFLINE;
    }
    
    @Override
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	transServer.Write(writer);
    	writer.WriteInt32(type);
    	writer.WriteInt64(id);
    }
}