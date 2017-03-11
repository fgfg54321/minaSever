package org.apache.mina.tcp.base.transserver.protocol.transmission;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectBase;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class TServerToLogicTransWriter extends TCPBaseWriter
{
	
	public ConnectBase   connectBase;
	public byte[]        datas;
	public long          dstServerId;
	
	
    public TServerToLogicTransWriter()
    {
    
    }
    
    public TServerToLogicTransWriter(ConnectBase connectBase,byte[] datas)
    {
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
    
    @Override
    public long GetSrcServerId()
    {
    	return TServerConfig.SERVER_ID;
    }
    
    @Override
    public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_TRANS;
    }
    
    @Override
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	connectBase.Write(writer);
		writer.WriteBytes(datas);
    }
	 
}
