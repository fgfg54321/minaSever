package org.apache.mina.tcp.base.transserver.codec.decoder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.transserver.TServerConfig;
import org.apache.mina.tcp.base.transserver.TransServerManager;
import org.apache.mina.tcp.base.transserver.protocol.connect.TServerDSReceiveConnectReader;
import org.apache.mina.tcp.base.transserver.protocol.tick.TServerReceiveTServerDSOffLineReader;
import org.apache.mina.tcp.base.transserver.protocol.transmission.TServerToTServerTransReader;

public class TransServerDecoderHandler extends DecoderHandler
{
	public boolean IsMeet(IoSession session)
	{
		boolean isTServer = TransServerManager.IsTServer(session);
		return isTServer;
	}
	
	public boolean Decode(ProtocolStreamReader reader,IoSession session)
	{
	  
        TCPBaseReader tcpReader        = new TCPBaseReader();
     	tcpReader.ReadHeader(reader);
     	
     	int dstServer                  = tcpReader.GetDstServerId();
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
	 					tcpReader = new TServerDSReceiveConnectReader();
	     				tcpReader.Read(reader,session);
	     				
	     				break;
					}

	     			case TServerConfig.MESSAGE_OFFLINE:
	     			{
	     				tcpReader = new TServerReceiveTServerDSOffLineReader();
	     				tcpReader.Read(reader,session);
	     				
	     				break;
	     			}
 				
				}
	 		}
	 		
		}
		else
		{
 			switch(messageId)
 			{
     			case TServerConfig.MESSAGE_TRANS:
     			{
     				tcpReader = new TServerToTServerTransReader();
     				tcpReader.Read(reader,session);
     				
     				break;
     			}
     				
     	     }
		}
	         
	    
	     return true;
	}
}
