package org.apache.mina.logic.chat.codec.server.answer;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.logicserver.protocol.tick.LogicReceiveTServerOffLineInfoReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.ConnectClient;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class ServerAnswerReader extends TCPBaseReader
{
	public ConcurrentHashMap<Long, ConcurrentHashMap<Long,TCPBaseReader>> idReaderCacheDic = new ConcurrentHashMap<Long, ConcurrentHashMap<Long,TCPBaseReader>>();
	
	public ConnectClient connectClient;
    public byte[]        datas;
	
    public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_TRANS;
    }
    
    public  void ReadContent(ProtocolStreamReader reader)
    {
    	connectClient.Read(reader);
    	datas = reader.ReadToEnd();
    }
    
 
    public void OnReader(IoSession session,Object param)
    {
    	int dstServerId              = connectClient.dstServerId;
    	long uid                     = connectClient.id;
    	ProtocolStreamReader reader  = new ProtocolStreamReader(datas);
    	
    	TCPBaseReader tcpReader      = new TCPBaseReader();
    	tcpReader.ReadHeader(reader);
    	
    	long uniqueId                = tcpReader.GetUniqueId();
     	int messageId                = tcpReader.GetMessageId();
     	
     	if(idReaderCacheDic.containsKey(uid))
     	{
     		ConcurrentHashMap<Long,TCPBaseReader> readerCacheDic = idReaderCacheDic.get(uid);
     		if(readerCacheDic.containsKey(uniqueId))
     		{
     			tcpReader = readerCacheDic.get(uniqueId);
     		}
     	}
     	
     	reader.Reset();
		switch(messageId)
		{
			case TServerConfig.MESSAGE_LOGIN:
			{
				tcpReader = new ClientLoginReader();
				
				break;
			}
			case TServerConfig.MESSAGE_OFFLINE:
			{
				tcpReader = new LogicReceiveTServerOffLineInfoReader();
				
				break;
			}
				
	     }
   
		if(!tcpReader.Read(reader,session))
		{
			ConcurrentHashMap<Long,TCPBaseReader> readerCacheDic = null;
			if(!idReaderCacheDic.containsKey(uid))
	     	{
				readerCacheDic = new ConcurrentHashMap<Long,TCPBaseReader>();
				idReaderCacheDic.put(uid, readerCacheDic);
	     	}
			else
			{
				readerCacheDic = idReaderCacheDic.get(uid);
			}
			
			if(!readerCacheDic.containsKey(uniqueId))
			{
				readerCacheDic.put(uniqueId, tcpReader);
			}
		}
    }
}
