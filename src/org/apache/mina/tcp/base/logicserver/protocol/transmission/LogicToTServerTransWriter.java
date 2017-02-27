package org.apache.mina.tcp.base.logicserver.protocol.transmission;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.logicserver.LogicConfig;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectBase;
import org.apache.mina.tcp.base.struct.ConnectClient;
import org.apache.mina.tcp.base.struct.ConnectLServer;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class LogicToTServerTransWriter extends TCPBaseWriter
{
    /*
     * type 0 client 1 server
     */
    public int            type;
    public long           id;
    public ConnectClient  connectClient;
    public ConnectLServer connectServer;
    public ConnectBase    connectBase;
    public byte[]         datas;
    
    public LogicToTServerTransWriter()
    {
    
    }
  
    public LogicToTServerTransWriter(int type,int id,byte[] datas)
    {
    	this.type           = type;
    	this.id             = id;
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
    	writer.WriteInt32(type);
    	connectBase.Write(writer);
    	if(type == LogicConfig.TYPE_CLIENT)
    	{
    		connectClient.Write(writer);
    	}
    	else if(type == LogicConfig.TYPE_LSERVER)
    	{
    		connectServer.Write(writer);
    	}
    	writer.WriteBytes(datas);
    }
}
