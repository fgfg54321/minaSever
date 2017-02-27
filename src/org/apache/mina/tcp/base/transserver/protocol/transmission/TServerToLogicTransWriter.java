package org.apache.mina.tcp.base.transserver.protocol.transmission;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectClient;
import org.apache.mina.tcp.base.struct.ConnectLServer;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class TServerToLogicTransWriter extends TCPBaseWriter
{
	
	public ConnectClient client;
	public ConnectLServer server;
	public byte[]        datas;
	  
	/*
	 * type 0 client 1 server
	 */
	public int           type;
	
    public TServerToLogicTransWriter()
    {
    
    }
    
    public TServerToLogicTransWriter(ConnectClient cClient,byte[] datas)
    {
    	this.client         = cClient;
    	this.datas          = datas;
    	this.type           = TServerConfig.TYPE_CLIENT;
    }
    
    public TServerToLogicTransWriter(ConnectLServer cServer,byte[] datas)
    {
    	this.server         = cServer;
    	this.datas          = datas;
    	this.type           = TServerConfig.TYPE_LSERVER;
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
    	if(type == TServerConfig.TYPE_CLIENT)
    	{
    		client.Write(writer);
    	}
    	else if(type == TServerConfig.TYPE_LSERVER)
    	{
    		server.Write(writer);
    	}

		writer.WriteBytes(datas);
    }
	 
}
