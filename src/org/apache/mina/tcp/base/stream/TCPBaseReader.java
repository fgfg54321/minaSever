package org.apache.mina.tcp.base.stream;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.log.MLog;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.utils.Action;
import org.apache.mina.utils.SVZipUtils;

public class TCPBaseReader
{
	protected long    uniqueId;
	protected int     srcServerId;
	protected int     dstServerId;
	protected int     messageId;
	protected boolean gzip;
	protected int     sliceSize;

	public SplitPackage       splitData;
	public ConcurrentHashMap<Integer, SplitPackage> splitDataDic = new ConcurrentHashMap<Integer, SplitPackage>();

	public Action<TCPBaseReader> onReaderAction;

	public long GetUniqueId()
    {
    	return uniqueId;
    }
	
    public int GetSrcServerId()
    {
    	return srcServerId;
    }
    
    public int GetDstServerId()
    {
    	return dstServerId;
    }
    
    public int GetMessageId()
    {
    	return messageId;
    }
    
    public boolean IsGZip()
    {
    	return gzip;
    }
    
    public void ReadHeader(ProtocolStreamReader reader)
    {
        srcServerId         = reader.ReadInt32();
        dstServerId         = reader.ReadInt32();
        messageId           = reader.ReadInt32();   
        uniqueId            = reader.ReadInt32();
        gzip                = reader.ReadBoolean();
        sliceSize           = reader.ReadInt32();
    }
    

    public  void ReadContent(ProtocolStreamReader reader)
    {

    }
    

    public boolean IsReceiveFull()
    {
        if (splitDataDic.size() == 0)
        {
            return false;
        }

        return splitDataDic.size() == sliceSize;
    }
    
    public boolean Combine(TCPBaseReader tcpReader,IoSession session)
    {
		 long uId = tcpReader.GetUniqueId();
		 if(uId == uniqueId)
		 {
			 SplitPackage sData   = tcpReader.splitData;
			 int index            = sData.index;
			 if (!splitDataDic.containsKey(index))
		     {
		         splitDataDic.put(index, sData);
		     }
		     else
		     {
		     	MLog.Error(String.format("uniqueId %d messageId %d index %d repeat", uniqueId, Integer.toHexString(messageId), index));
		     }
		     
		     if (IsReceiveFull())
		     {
		     	ProtocolStreamWriter writer = new ProtocolStreamWriter();
		        ArrayList<Byte> dataArr     = new ArrayList<Byte>();
		        for (int i = 0; i < splitDataDic.size(); i++)
		        {
		             byte[] unGzipDatas = splitDataDic.get(i).datas;
		             for(int j = 0 ; j < unGzipDatas.length; j++)
		             {
		             	dataArr.add(unGzipDatas[j]);
		             }
		             writer.WriteBytes(unGzipDatas);
		         }
		
		         byte[] datas = writer.GetBuffer();
		         
		         if (gzip)
		         {
		             datas = SVZipUtils.ConvertBytesGZip(datas, SVZipUtils.CompressionMode.Decompress);
		         }
		         
		         if(splitData != null)
		         {
		             splitData.Dispose();
		             splitData = null;
		         }
		
		         for(int i = 0; i < splitDataDic.size(); i++)
		         {
		             if(splitDataDic.get(i) != null)
		             {
		                 splitDataDic.get(i).Dispose();
		             }
		         }
		
		         splitDataDic.clear();
		         
		         
		         ProtocolStreamReader allReader = new ProtocolStreamReader(datas);
		         ReadContent(allReader);
		         session.write(this);
		         
		         return true;
		     }
		     
		 }
	     
	     return false;
    }
     
    public boolean Read(ProtocolStreamReader reader,IoSession ioSession)
    {
    	ReadHeader(reader);
    	
        int  sliceIndex          = reader.ReadInt32();
        
        SplitPackage sliceData   = new SplitPackage();
        sliceData.index          = sliceIndex;
        sliceData.srcServerId    = srcServerId;
        sliceData.dstServerId    = dstServerId;
        sliceData.messageId      = messageId;
        byte[] enddata           = reader.ReadToEnd();
        sliceData.datas          = enddata;
        
        if (!splitDataDic.containsKey(sliceIndex))
        {
            splitDataDic.put(sliceIndex, sliceData);
        }
        else
        {
        	MLog.Error(String.format("uniqueId %d messageId %d index %d repeat", uniqueId, Integer.toHexString(messageId), sliceIndex));
        }
        
        if (IsReceiveFull())
        {
        	ProtocolStreamWriter writer = new ProtocolStreamWriter();
            ArrayList<Byte> dataArr     = new ArrayList<Byte>();
            for (int i = 0; i < splitDataDic.size(); i++)
            {
                byte[] unGzipDatas = splitDataDic.get(i).datas;
                for(int j = 0 ; j < unGzipDatas.length; j++)
                {
                	dataArr.add(unGzipDatas[j]);
                }
                writer.WriteBytes(unGzipDatas);
            }

            byte[] datas = writer.GetBuffer();
            
            if (gzip)
            {
                datas = SVZipUtils.ConvertBytesGZip(datas, SVZipUtils.CompressionMode.Decompress);
            }
            
            Dispose();
            
            ProtocolStreamReader allReader = new ProtocolStreamReader(datas);
            ReadContent(allReader);
            ioSession.write(this);
            
            return true;
        }
        
        return false;
        
    }
    

    
    public void  OnReader(IoSession session,Object param)
    {
    	
    }
    

    public void Dispose()
    {
    	if(splitData != null)
        {
            splitData.Dispose();
            splitData = null;
        }

        for(int i = 0; i < splitDataDic.size(); i++)
        {
            if(splitDataDic.get(i) != null)
            {
                splitDataDic.get(i).Dispose();
            }
        }

        splitDataDic.clear();
    }
    
}