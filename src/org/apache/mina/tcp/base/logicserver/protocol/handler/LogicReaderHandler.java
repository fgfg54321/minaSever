package org.apache.mina.tcp.base.logicserver.protocol.handler;

import java.util.HashMap;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.logicserver.LogicConfig;
import org.apache.mina.tcp.base.logicserver.protocol.customer.LogicBaseReader;
import org.apache.mina.tcp.base.logicserver.protocol.transmission.LogicToTServerTransReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;

public class LogicReaderHandler
{

	public static HashMap<Integer,Class<LogicBaseReader>> clientMsgMaper = new  HashMap<Integer,Class<LogicBaseReader>>();
	
	public static HashMap<Integer,Class<LogicBaseReader>> serverMsgMaper = new  HashMap<Integer,Class<LogicBaseReader>>();
	
	public static void Initialize()
	{
		
	}
	
	
	public boolean ReaderHander(LogicToTServerTransReader transTcpReader,int messageId,ProtocolStreamReader reader,IoSession ioSession)
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
					return innerReader.Read(reader, ioSession);
				}
			}
			else if(type == LogicConfig.TYPE_LSERVER)
			{
				if(serverMsgMaper.containsKey(messageId))
				{
					Class<LogicBaseReader> readerClass = serverMsgMaper.get(messageId);
					LogicBaseReader innerReader        = readerClass.newInstance();
					transTcpReader.SetInnerTcpReader(innerReader);
					return innerReader.Read(reader, ioSession);
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
