package org.apache.mina.tcp.base.logicserver.protocol.transmission;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.logicserver.LogicConfig;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectBase;
import org.apache.mina.tcp.base.struct.ConnectLServer;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class LogicToTServerTransWriter extends TCPBaseWriter
{

	public ConnectLServer connectLServer;
    public ConnectBase    connectBase;
    
    protected byte[] datas;
    
    public LogicToTServerTransWriter()
    {
    
    }
  
    public LogicToTServerTransWriter(ConnectLServer connectLServer,ConnectBase connectBase,byte[] datas)
    {
    	this.connectLServer = connectLServer;
    	this.connectBase    = connectBase;
    	this.datas          = datas;
    }
    
    @Override
    public long GetSrcServerId()
    {
    	return LogicConfig.SERVER_ID;
    }
    
    @Override
    public long GetDstServerId()
    {
    	return TServerConfig.SERVER_ID;
    }
    
    
    @Override
    public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_TRANS;
    }
    
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	connectLServer.Write(writer);
    	connectBase.Write(writer);
    	writer.WriteBytes(datas);
    }
}
