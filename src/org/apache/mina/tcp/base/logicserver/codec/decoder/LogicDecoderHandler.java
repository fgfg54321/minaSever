package org.apache.mina.tcp.base.logicserver.codec.decoder;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.handler.DecoderHandler;
import org.apache.mina.tcp.base.logicserver.LogicConfig;
import org.apache.mina.tcp.base.logicserver.LogicServerManager;
import org.apache.mina.tcp.base.logicserver.protocol.connect.TServerConnectReader;
import org.apache.mina.tcp.base.logicserver.protocol.transmission.LogicToTServerTransReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.transserver.TransServerManager;
import org.apache.mina.tcp.base.transserver.protocol.tick.TServerNoticeLogicOffLineInfoReader;

public class LogicDecoderHandler extends DecoderHandler
{
	public ConcurrentHashMap<Long, ConcurrentHashMap<Long,TCPBaseReader>> clientReaderCacheDic = new ConcurrentHashMap<Long, ConcurrentHashMap<Long,TCPBaseReader>>();
	
	public ConcurrentHashMap<Long, ConcurrentHashMap<Long,TCPBaseReader>> serverReaderCacheDic = new ConcurrentHashMap<Long, ConcurrentHashMap<Long,TCPBaseReader>>();
	
	@Override
	public boolean IsMeet(IoSession session)
	{
		boolean isServer = LogicServerManager.IsConnectTServer(session);
		return isServer;
	}
	
	@Override
	public boolean Decode(ProtocolStreamReader reader,IoSession session,ProtocolDecoderOutput out)
	{
	   
        TCPBaseReader tcpReader        = new TCPBaseReader();
     	tcpReader.ReadHeader(reader);
     	
     	long dstServer                 = tcpReader.GetDstServerId();
    	int messageId                  = tcpReader.GetMessageId();
 		if(dstServer == LogicConfig.SERVER_ID)
 		{
 			reader.Reset();
 			if(!TransServerManager.IsLogin(session))
			{
 				switch(messageId)
	 			{
	     			case LogicConfig.MESSAGE_CONNECT_LOGIN:
	     			{
	     				tcpReader = new TServerConnectReader();
	     				tcpReader.Read(reader,session,out);
	     				
	     				break;
	     			}
	     			case LogicConfig.MESSAGE_CONNECT_OFFLINE:
	     			{
	     				tcpReader = new TServerNoticeLogicOffLineInfoReader();
	     				OffLineReader(tcpReader,reader,session,out);
	     				
	     				break;
	     			}
	     				
	     	     }
			}
 			else
 			{
 				switch(messageId)
	 			{
		 			case LogicConfig.MESSAGE_CONNECT_TRANS:
		 			{
		 				tcpReader = new LogicToTServerTransReader();
		 				TransReader(tcpReader, reader, session,out);
		 				
		 				break;
		 			}
	 			}
 			}
 			
	     }
 		
	     return true;
	}
	
	protected void OffLineReader(TCPBaseReader tcpReader,ProtocolStreamReader reader,IoSession session,ProtocolDecoderOutput out)
	{
		if(!tcpReader.Read(reader,session,out))
		{
			TServerNoticeLogicOffLineInfoReader offLineReader = (TServerNoticeLogicOffLineInfoReader)tcpReader;
			int type = offLineReader.GetType();
			long id  = offLineReader.GetId();
			ConcurrentHashMap<Long, ConcurrentHashMap<Long,TCPBaseReader>> cacheReaderDic = null;
			if(type == LogicConfig.TYPE_CLIENT)
	    	{
				cacheReaderDic = clientReaderCacheDic;
	    	}
			else if(type == LogicConfig.TYPE_LSERVER)
			{
				cacheReaderDic = serverReaderCacheDic;
			}
			
			if(cacheReaderDic == null)
			{
				return;
			}
			
			if(cacheReaderDic.containsKey(id))
	     	{
				cacheReaderDic.remove(id);
	     	}
		}
	}
	
	
	protected void TransReader(TCPBaseReader tcpReader,ProtocolStreamReader reader,IoSession session,ProtocolDecoderOutput out)
	{
		if(!tcpReader.Read(reader,session,out))
		{
			LogicToTServerTransReader transReader = (LogicToTServerTransReader)tcpReader;
			
			int type      = transReader.GetType();
			long uniqueId = transReader.GetInnerUniqueId();
			long id       = transReader.GetId();
			ConcurrentHashMap<Long, ConcurrentHashMap<Long,TCPBaseReader>> cacheReaderDic = null;
			
			if(type == LogicConfig.TYPE_CLIENT)
	    	{
				cacheReaderDic = clientReaderCacheDic;
	    	}
			else if(type == LogicConfig.TYPE_LSERVER)
			{
				cacheReaderDic = serverReaderCacheDic;
			}
			
			if(cacheReaderDic == null)
			{
				return;
			}
			
	     	if(cacheReaderDic.containsKey(id))
	     	{
	     		ConcurrentHashMap<Long,TCPBaseReader> readerCacheDic = cacheReaderDic.get(id);
	     		if(readerCacheDic.containsKey(uniqueId))
	     		{
	     			TCPBaseReader saveTcpReader               = readerCacheDic.get(uniqueId);
	     			LogicToTServerTransReader saveTransReader = (LogicToTServerTransReader)saveTcpReader;
	     			if(saveTransReader.Combine(transReader, session,out))
	     			{
	     				cacheReaderDic.remove(uniqueId);
	     			}
	     		}
	     		else
	     		{
	     			readerCacheDic.put(uniqueId, transReader);
	     		}
	     	}
	     	else
	     	{
	     		ConcurrentHashMap<Long,TCPBaseReader> readerCacheDic = null;
				if(!cacheReaderDic.containsKey(id))
		     	{
					readerCacheDic = new ConcurrentHashMap<Long,TCPBaseReader>();
					cacheReaderDic.put(id, readerCacheDic);
		     	}
				else
				{
					readerCacheDic = cacheReaderDic.get(id);
				}
				
				if(!readerCacheDic.containsKey(uniqueId))
				{
					readerCacheDic.put(uniqueId, transReader);
				}
	     	}
		}
	}
}
