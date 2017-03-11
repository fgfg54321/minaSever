package org.apache.mina.tcp.base.client.codec.decoder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.client.ClientConfig;
import org.apache.mina.tcp.base.client.protocol.connect.ClientConnectReader;
import org.apache.mina.tcp.base.client.protocol.tick.ClientOffLineReader;
import org.apache.mina.tcp.base.handler.DecoderHandler;
import org.apache.mina.tcp.base.logicserver.protocol.transmission.LogicToTServerTransReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.transserver.TransServerManager;

public class ClientDecoderHandler extends DecoderHandler
{
	public boolean IsMeet(IoSession session)
	{
		return true;
	}
	
	public boolean Decode(ProtocolStreamReader reader,IoSession session,ProtocolDecoderOutput out)
	{
        TCPBaseReader tcpReader        = new TCPBaseReader();
     	tcpReader.ReadHeader(reader);
     	
     	long srcServer                 = tcpReader.GetSrcServerId();
     	int messageId                  = tcpReader.GetMessageId();
     	

		reader.Reset();
     	if(srcServer == ClientConfig.CONNECT_SERVER_ID)
     	{
     		switch(messageId)
    		{
     			case ClientConfig.CONNECT_MESSAGE_LOGIN:
     			{
     				tcpReader = new ClientConnectReader();
     				tcpReader.Read(reader,session,out);
     				
     				break;
     			}
     			case ClientConfig.CONNECT_MESSAGE_OFFLINE:
     			{
     				tcpReader = new ClientOffLineReader();
     				tcpReader.Read(reader,session,out);
     				
     				break;
     			}
     				
     	     }
     	}
     	else if(srcServer ==  ClientConfig.LOGIC_SERVER_ID)
     	{
     		switch(messageId)
    		{
     			case ClientConfig.LOGIC_MESSAGE_LOGIN:
     			{
     				tcpReader = new LogicToTServerTransReader();
     				tcpReader.Read(reader,session,out);
     				
     				break;
     			}
     	     }
     	}
		
	    return true;
	}
}
