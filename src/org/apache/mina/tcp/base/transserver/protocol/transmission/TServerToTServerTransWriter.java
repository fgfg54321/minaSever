package org.apache.mina.tcp.base.transserver.protocol.transmission;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectBase;
import org.apache.mina.tcp.base.struct.ConnectTServer;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class TServerToTServerTransWriter extends TCPBaseWriter
{
	public ConnectTServer connectTServer;
	public ConnectBase  connectBase;
	public byte[]       datas;
	public long         dstServerId; 
	  
    public TServerToTServerTransWriter()
    {
    
    }
    
    public TServerToTServerTransWriter(ConnectTServer tServer,ConnectBase connectBase,byte[] datas)
    {
    	this.connectTServer = tServer;
    	this.connectBase    = connectBase;
    	this.datas          = datas;
    }
    
    public void setDstServerId(long dstServerId)
    {
    	this.dstServerId = dstServerId;
    }
    		
    @Override
    public long GetDstServerId()
    {
    	return dstServerId;
    }
    
    public long GetSrcServerId()
    {
    	return TServerConfig.SERVER_ID;
    }
    
    public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_TRANS;
    }
    
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	connectTServer.Write(writer);
    	connectBase.Write(writer);
		writer.WriteBytes(datas);
    }
	 
}
