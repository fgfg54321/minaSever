package org.apache.mina.logic.chat.codec.decoder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.base.client.ClientTcpRequest;
import org.apache.mina.base.client.ClientTcpResponse;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.log.MLog;
import org.apache.mina.logic.chat.codec.NetDataPackage;
import org.apache.mina.logic.chat.codec.server.login.ServerLoginReader;
import org.apache.mina.logic.chat.codec.server.tick.ServerOffLineReader;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class ProtocolBaseDecoder extends CumulativeProtocolDecoder
{
      protected ConcurrentHashMap<Long,ConcurrentHashMap<Long,NetDataPackage>> idStreamDataDic = new  ConcurrentHashMap<Long,ConcurrentHashMap<Long,NetDataPackage>>();  
	
      protected HashMap<Long, NetDataPackage>           streamDataDic             = new HashMap<Long, NetDataPackage>();
      protected HashMap<Integer, Class<ClientTcpResponse>>    messageHandler      = new HashMap<Integer, Class<ClientTcpResponse>>();

	@Override
	  protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception
	  {
        int available = in.remaining();
        if(available > 4)
        {
        	int len = in.getInt();
        	if(len > in.remaining())
        	{
        		byte[] dataBytes       = new byte[len];
                in.get(dataBytes);
                ProtocolStreamReader sReader     = new ProtocolStreamReader(dataBytes);
                
                TCPBaseReader tcpRequest = new TCPBaseReader();
            	tcpRequest.Read(sReader,session);
            	
            	int dstServer               = tcpRequest.GetDstServerId();
        		if(dstServer == TServerConfig.SERVER_ID)
        		{
        			sReader.Reset();
        			switch(dstServer)
        			{
            			case TServerConfig.MESSAGE_LOGIN:
            			{
            				tcpRequest = new ServerLoginReader();
            				break;
            			}
            			case TServerConfig.MESSAGE_TRANS:
            			{
            				
            				break;
            			}
            			case TServerConfig.MESSAGE_OFFLINE:
            			{
            				tcpRequest = new ServerOffLineReader();
            				break;
            			}
            				
            	     }
        			tcpRequest.Read(sReader,session);
        		}
        		else
        		{
	                NetDataPackage netData = new NetDataPackage(dataBytes);
	                long id                = netData.uniqueId;
	                if (streamDataDic.containsKey(id))
	                {
	                    NetDataPackage sPackage = streamDataDic.get(id);
	                    sPackage.AddSplitData(netData.splitData);
	                }
	                else
	                {
	                    streamDataDic.put(id, netData);
	                }
	                
	                Iterator<Entry<Long, NetDataPackage>>  iter = streamDataDic.entrySet().iterator();
	                while (iter.hasNext())
	                {
	                	Entry<Long, NetDataPackage> entry = iter.next();
	                    long key                          = entry.getKey();
	                    NetDataPackage nPackage           = entry.getValue();
	                    
	                    if (nPackage.IsReceiveFull())
	                    {
	                        int messageId                 = nPackage.messageId;
	                        ProtocolStreamReader reader   = nPackage.CombineData();
	                        if (messageHandler.containsKey(messageId))
	                        {
	                        	ClientTcpResponse netResponse   = messageHandler.get(messageId).newInstance();
	                            try
	                            {
	                                netResponse.Read(reader);
	                            }
	                            catch(Exception e)
	                            {
	                                MLog.Error("%s,%s",e.getMessage(),e.getStackTrace().toString());
	                            }
	                            out.write(netResponse);
	                        }
	                        else
	                        {
	                        	MLog.Error("messageId {0} hander Response not exist", Integer.toHexString(messageId));
	                        }
	                        
	                        streamDataDic.remove(key);
	                    }
	                }
	                
	                return true;
        		}
        	}
        }

    	return false;
    }

}
