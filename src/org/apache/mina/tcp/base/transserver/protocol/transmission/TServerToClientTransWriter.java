package org.apache.mina.tcp.base.transserver.protocol.transmission;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class TServerToClientTransWriter extends TCPBaseWriter
{
    public byte[] datas;
    
    public TServerToClientTransWriter(byte[] datas)
    {
    	this.datas = datas;
    }
    
    @Override
    public int GetSrcServerId()
    {
    	return TServerConfig.SERVER_ID;
    }
    
    @Override
    public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_TRANS;
    }

    protected void WriteHeader(ProtocolStreamWriter writer)
    {
    	
    }
    
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	writer.WriteBytes(datas);
    }
	 
}
