package org.apache.mina.gate.codec;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.log.MLog;
import org.apache.mina.stream.ProtocolStreamReader;

public class ProtocolGateBaseDecoder extends CumulativeProtocolDecoder
{
          
      protected HashMap<Long, NetDataPackage>           streamDataDic       = new HashMap<Long, NetDataPackage>();
      protected HashMap<Integer, Class<GateBaseRequest>>    messageHandler      = new HashMap<Integer, Class<GateBaseRequest>>();

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
                            GateBaseRequest netResponse   = messageHandler.get(messageId).newInstance();
                            try
                            {
                                netResponse.BaseSerialize(reader);
                                netResponse.DeSerialize(reader);
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

    	return false;
    }

}
