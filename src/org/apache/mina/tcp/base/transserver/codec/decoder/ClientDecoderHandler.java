package org.apache.mina.tcp.base.transserver.codec.decoder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.handler.DecoderHandler;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.transserver.TServerConfig;
import org.apache.mina.tcp.base.transserver.TransServerManager;
import org.apache.mina.tcp.base.transserver.protocol.connect.ClientConnectReader;
import org.apache.mina.tcp.base.transserver.protocol.tick.TServerNoticeClientOffLineReader;
import org.apache.mina.tcp.base.transserver.protocol.transmission.TServerToClientTransReader;

public class ClientDecoderHandler extends DecoderHandler
{
	
	public boolean IsMeet(IoSession session)
	{
		boolean isClient = TransServerManager.IsClient(session);
		return isClient;
	}
	
	@Override
	public boolean Decode(ProtocolStreamReader reader,IoSession session,ProtocolDecoderOutput out)
	{
	
    	TCPBaseReader tcpReader        = new TCPBaseReader();
    	tcpReader.ReadHeader(reader);
    	
    	long dstServer                 = tcpReader.GetDstServerId();
    	int messageId                  = tcpReader.GetMessageId();
		
		reader.Reset();
		if(!TransServerManager.IsLogin(session))
		{	
			if(dstServer == TServerConfig.SERVER_ID)
			{
				switch(messageId)
				{
					case TServerConfig.MESSAGE_LOGIN:
					{
						tcpReader = new ClientConnectReader();
						tcpReader.Read(reader,session,out);
						break;
					}
					case TServerConfig.MESSAGE_OFFLINE:
					{
						tcpReader = new TServerNoticeClientOffLineReader();
						tcpReader.Read(reader,session,out);
						
						break;
					}
				}
			}
		}
		else
		{
			
			tcpReader = new TServerToClientTransReader();
			tcpReader.Read(reader,session,out);
			
		}
    		
       return true;
	}
}
