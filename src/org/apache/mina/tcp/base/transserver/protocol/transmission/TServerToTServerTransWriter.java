package org.apache.mina.tcp.base.transserver.protocol.transmission;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectClient;
import org.apache.mina.tcp.base.struct.ConnectLServer;
import org.apache.mina.tcp.base.struct.ConnectTServer;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class TServerToTServerTransWriter extends TCPBaseWriter
{
	
	public ConnectClient  client;
	public ConnectLServer lServer;
	public ConnectTServer tServer;
	public byte[]         datas;
	  
	/*
	 * type 0 client 1 server
	 */
	public int           type;
	
    public TServerToTServerTransWriter()
    {
    
    }
    
    public TServerToTServerTransWriter(ConnectClient cClient,byte[] datas)
    {
    	this.client         = cClient;
    	this.datas          = datas;
    	this.type           = TServerConfig.TYPE_CLIENT;
    }
    
    public TServerToTServerTransWriter(ConnectLServer cServer,byte[] datas)
    {
    	this.lServer        = cServer;
    	this.datas          = datas;
    	this.type           = TServerConfig.TYPE_LSERVER;
    }
    
    public TServerToTServerTransWriter(ConnectTServer tServer,byte[] datas)
    {
    	this.tServer        = tServer;
    	this.datas          = datas;
    	this.type           = TServerConfig.TYPE_TSERVER;
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
    		lServer.Write(writer);
    	}
    	else if(type == TServerConfig.TYPE_TSERVER)
    	{
    		tServer.Write(writer);
    	}

		writer.WriteBytes(datas);
    }
	 
}
