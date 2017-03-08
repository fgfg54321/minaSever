package org.apache.mina.tcp.base.logicserver.protocol.transmission;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectBase;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class LogicToTServerTransWriter extends TCPBaseWriter
{

    public ConnectBase    connectBase;
    
    protected byte[] datas;
    
    public LogicToTServerTransWriter()
    {
    
    }
  
    public LogicToTServerTransWriter(ConnectBase connectBase,byte[] datas)
    {
    	this.connectBase  = connectBase;
    	this.datas        = datas;
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
