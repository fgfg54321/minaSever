package org.apache.mina.logic.chat.codec.server.answer;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectClient;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class ServerAnswerWriter extends TCPBaseWriter
{
	public ConnectClient connectClient;
    public byte[]        datas;
    public int           srcServerId;
    public int           dstServerId;
	    
    public ServerAnswerWriter()
    {
    
    }
    
    public ServerAnswerWriter(ConnectClient cClient,byte[] datas)
    {
    	this.connectClient  = cClient;
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
    	connectClient.Write(writer);
    	writer.WriteBytes(datas);
    }
}
