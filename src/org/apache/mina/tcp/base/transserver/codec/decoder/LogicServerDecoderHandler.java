package org.apache.mina.tcp.base.transserver.codec.decoder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.handler.DecoderHandler;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.transserver.TServerConfig;
import org.apache.mina.tcp.base.transserver.TransServerManager;
import org.apache.mina.tcp.base.transserver.protocol.connect.LogicConnectReader;
import org.apache.mina.tcp.base.transserver.protocol.tick.TServerNoticeLogicOffLineInfoReader;
import org.apache.mina.tcp.base.transserver.protocol.transmission.TServerToLogicTransReader;

public class LogicServerDecoderHandler extends DecoderHandler
{
	public boolean IsMeet(IoSession session)
	{
		boolean isServer = TransServerManager.IsLServer(session);
		return isServer;
	}
	@Override
	public boolean Decode(ProtocolStreamReader reader,IoSession session,ProtocolDecoderOutput out)
	{
	    
        TCPBaseReader tcpReader        = new TCPBaseReader();
     	tcpReader.ReadHeader(reader);
     	
     	long dstServer                 = tcpReader.GetDstServerId();
    	int messageId                  = tcpReader.GetMessageId();
 		
		reader.Reset();
		
		if(dstServer == TServerConfig.SERVER_ID)
 		{
			switch(messageId)
			{
			
				case TServerConfig.MESSAGE_LOGIN:
				{
 					tcpReader = new LogicConnectReader();
     				tcpReader.Read(reader,session,out);
     				
     				break;
     			}
				case TServerConfig.MESSAGE_OFFLINE:
     			{
     				tcpReader = new TServerNoticeLogicOffLineInfoReader();
     				tcpReader.Read(reader,session,out);
     				
     				break;
     			}
     			default:
     			{
     				if(TransServerManager.IsLogin(session))
     				{
     					tcpReader = new TServerToLogicTransReader();
     					tcpReader.Read(reader,session,out);
     				}
     				break;
     			}
			}
 		}
		else
		{
			if(TransServerManager.IsLogin(session))
			{
				tcpReader = new TServerToLogicTransReader();
				tcpReader.Read(reader,session,out);
			}
		}
	 		
	  return true;
	}
}
