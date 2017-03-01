package org.apache.mina.tcp.base.logicserver.protocol.transmission;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.log.MLog;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.logicserver.protocol.customer.LogicBaseReader;
import org.apache.mina.tcp.base.logicserver.protocol.handler.LogicReaderHandler;
import org.apache.mina.tcp.base.stream.SplitPackage;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.ConnectBase;
import org.apache.mina.tcp.base.transserver.TServerConfig;
import org.apache.mina.utils.SVZipUtils;

public class LogicToTServerTransReader extends TCPBaseReader
{

	protected ConnectBase  connectBase;
	
	/*
	 * 0 client 1 server
	 */
	protected int    type;
	protected byte[] datas;
    
    protected LogicBaseReader innerTcpReader;
    
    protected LogicReaderHandler readerHandler = new LogicReaderHandler();
    
    public int GetType()
    {
    	return type;
    }
    
    public long GetId()
    {
    	if(connectBase != null)
    	{
    		return connectBase.id;
    	}
    	return -1;
    }
    
    public long GetInnerUniqueId()
    {
    	if(innerTcpReader != null)
    	{
    		return innerTcpReader.GetUniqueId();
    	}
    	return -1;
    }
    
    
    public void SetInnerTcpReader(LogicBaseReader innerReader)
    {
    	innerTcpReader = innerReader;
    	innerTcpReader.SetConnectInfo(connectBase);
    }
    
    @Override
    public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_TRANS;
    }
    
    @Override
    public  void ReadContent(ProtocolStreamReader reader)
    {
    	connectBase = ConnectBase.ConnectFactory(reader);
    	datas       = reader.ReadToEnd();
    }
    
    @Override
    public boolean Combine(TCPBaseReader tReader,IoSession session)
    {
    	LogicToTServerTransReader transTcpReader = (LogicToTServerTransReader)tReader;
    	 TCPBaseReader tcpReader  = transTcpReader.innerTcpReader;
		 long uId                 = tcpReader.GetUniqueId();
		 long cId                 = innerTcpReader.GetUniqueId();
		 
		 ConcurrentHashMap<Integer, SplitPackage> cSplitDataDic = innerTcpReader.splitDataDic;
		 if(uId == cId)
		 {
			 SplitPackage sData   = tcpReader.splitData;
			 int index            = sData.index;
			 if (!cSplitDataDic.containsKey(index))
		     {
				 cSplitDataDic.put(index, sData);
		     }
		     else
		     {
		     	MLog.Error(String.format("uniqueId %d messageId %d index %d repeat", uId, Integer.toHexString(sData.messageId), index));
		     }
		     
		     if (IsReceiveFull())
		     {
		     	ProtocolStreamWriter writer = new ProtocolStreamWriter();
		        ArrayList<Byte> dataArr     = new ArrayList<Byte>();
		        for (int i = 0; i < cSplitDataDic.size(); i++)
		        {
		             byte[] unGzipDatas = cSplitDataDic.get(i).datas;
		             for(int j = 0 ; j < unGzipDatas.length; j++)
		             {
		             	dataArr.add(unGzipDatas[j]);
		             }
		             writer.WriteBytes(unGzipDatas);
		         }
		
		         byte[] datas = writer.GetBuffer();
		         
		         boolean gzip = innerTcpReader.IsGZip();
		         if (gzip)
		         {
		             datas = SVZipUtils.ConvertBytesGZip(datas, SVZipUtils.CompressionMode.Decompress);
		         }
		         
		         innerTcpReader.Dispose();
		         
		         ProtocolStreamReader allReader = new ProtocolStreamReader(datas);
		         innerTcpReader.Read(allReader, session);
		         
		         return true;
		     }
		     
		 }
	     return false;
    }
    
    @Override
    public boolean Read(ProtocolStreamReader spuerReader,IoSession session)
    {
    	super.Read(spuerReader,session);
    	
    	ProtocolStreamReader reader  = new ProtocolStreamReader(datas);
    	
    	innerTcpReader               = new LogicBaseReader();
    	innerTcpReader.ReadHeader(reader);
    	
     	int messageId                = innerTcpReader.GetMessageId();

     	reader.Reset();
		if(readerHandler.ReaderHander(this, messageId, reader, session))
		{
			return false;
		}
		
		return true;
    }
    
    @Override
    public void OnReader(IoSession session,Object param)
    { 
    	/*
    	if(innerTcpReader != null)
    	{
    		innerTcpReader.OnReader(session, param);
    	}
    	*/
    }
    
}
