package org.apache.mina.tcp.base.logicserver.codec.decoder;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.logicserver.LogicConfig;
import org.apache.mina.tcp.base.logicserver.protocol.transmission.LogicToTServerTransReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.TransServerManager;
import org.apache.mina.tcp.base.transserver.TServerConfig;
import org.apache.mina.tcp.base.transserver.protocol.tick.TServerNoticeLogicOffLineInfoReader;

public class LogicDecoderHandler extends DecoderHandler
{
	public ConcurrentHashMap<Long, ConcurrentHashMap<Long,TCPBaseReader>> clientReaderCacheDic = new ConcurrentHashMap<Long, ConcurrentHashMap<Long,TCPBaseReader>>();
	
	public ConcurrentHashMap<Long, ConcurrentHashMap<Long,TCPBaseReader>> serverReaderCacheDic = new ConcurrentHashMap<Long, ConcurrentHashMap<Long,TCPBaseReader>>();
	
	public boolean IsMeet(IoSession session)
	{
		boolean isServer = TransServerManager.IsLServer(session);
		return isServer;
	}
	
	public boolean Decode(ProtocolStreamReader reader,IoSession session)
	{
	   
        TCPBaseReader tcpReader        = new TCPBaseReader();
     	tcpReader.ReadHeader(reader);
     	
     	int dstServer                  = tcpReader.GetDstServerId();
    	int messageId                  = tcpReader.GetMessageId();
 		if(dstServer == LogicConfig.SERVER_ID)
 		{
 			reader.Reset();
 			if(!TransServerManager.IsLogin(session))
			{
 				switch(messageId)
	 			{
	     			case TServerConfig.MESSAGE_TRANS:
	     			{
	     				tcpReader = new LogicToTServerTransReader();
	     				TransReader(tcpReader, reader, session);
	     				
	     				break;
	     			}
	     			case TServerConfig.MESSAGE_OFFLINE:
	     			{
	     				tcpReader = new TServerNoticeLogicOffLineInfoReader();
	     				OffLineReader(tcpReader,reader,session);
	     				
	     				break;
	     			}
	     				
	     	     }
			}
 			
	     }
 		
	     return true;
	}
	
	protected void OffLineReader(TCPBaseReader tcpReader,ProtocolStreamReader reader,IoSession session)
	{
		if(!tcpReader.Read(reader,session))
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
	
	
	protected void TransReader(TCPBaseReader tcpReader,ProtocolStreamReader reader,IoSession session)
	{
		if(!tcpReader.Read(reader,session))
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
	     			if(saveTransReader.Combine(transReader, session))
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
