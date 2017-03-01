package org.apache.mina.tcp.base.transserver.protocol.transmission;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectBase;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class TServerToLogicTransWriter extends TCPBaseWriter
{
	
	public ConnectBase   connectBase;
	public byte[]        datas;
	
	
    public TServerToLogicTransWriter()
    {
    
    }
    
    public TServerToLogicTransWriter(ConnectBase connectBase,byte[] datas)
    {
    	this.connectBase    = connectBase;
    	this.datas          = datas;
    }
    
    public int GetSrcServerId()
    {
    	return TServerConfig.SERVER_ID;
    }
    
    public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_TRANS;
    }
    
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	connectBase.Write(writer);
		writer.WriteBytes(datas);
    }
	 
}
