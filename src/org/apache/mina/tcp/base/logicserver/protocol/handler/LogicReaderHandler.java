package org.apache.mina.tcp.base.logicserver.protocol.handler;

import java.util.HashMap;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.logicserver.LogicConfig;
import org.apache.mina.tcp.base.logicserver.protocol.customer.LogicBaseReader;
import org.apache.mina.tcp.base.logicserver.protocol.customer.client.login.LogicClientLoginReader;
import org.apache.mina.tcp.base.logicserver.protocol.transmission.LogicToTServerTransReader;

public class LogicReaderHandler
{

	public static HashMap<Integer,Class> clientMsgMaper = new  HashMap<Integer,Class>();
	
	public static HashMap<Integer,Class> serverMsgMaper = new  HashMap<Integer,Class>();
	
	public static void Initialize()
	{
		clientMsgMaper.put(LogicConfig.MESSAGE_LOGIC_LOGIN, LogicClientLoginReader.class);
	}
	
	
	public boolean ReaderHander(LogicToTServerTransReader transTcpReader,int messageId,ProtocolStreamReader reader,IoSession ioSession,ProtocolDecoderOutput out)
	{
		try
		{
			int type = transTcpReader.GetType();
			if(type == LogicConfig.TYPE_CLIENT)
			{
				if(clientMsgMaper.containsKey(messageId))
				{
					Class<LogicBaseReader> readerClass = clientMsgMaper.get(messageId);
					LogicBaseReader innerReader      = readerClass.newInstance();
					transTcpReader.SetInnerTcpReader(innerReader);
					return innerReader.Read(reader, ioSession,out);
				}
			}
			else if(type == LogicConfig.TYPE_LSERVER)
			{
				if(serverMsgMaper.containsKey(messageId))
				{
					Class<LogicBaseReader> readerClass = serverMsgMaper.get(messageId);
					LogicBaseReader innerReader        = readerClass.newInstance();
					transTcpReader.SetInnerTcpReader(innerReader);
					return innerReader.Read(reader, ioSession,out);
				}
			}
		
		} 
     	catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
}
