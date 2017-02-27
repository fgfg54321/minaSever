package org.apache.mina.gate.codec;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.mina.log.MLog;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.utils.SVZipUtils;

public class NetDataPackage
{	
    public long uniqueId;
    public int  srcServerId;
    public int  messageId;
    public int  tag;
    public byte gzip;
    public int  split;

    public SplitPackage                  splitData;
    public HashMap<Integer, SplitPackage> splitDataDic = new HashMap<Integer, SplitPackage>();

    public NetDataPackage(byte[] datas)
    {
    	ProtocolStreamReader reader = new ProtocolStreamReader(datas);
    	
        uniqueId              = reader.ReadInt64();
        srcServerId           = reader.ReadInt32();
        messageId             = reader.ReadInt32();
        gzip                  = reader.ReadByte();
        split                 = reader.ReadInt32();
        int index             = reader.ReadInt32();
        byte[] enddata        = reader.ReadToEnd();

        splitData             = new SplitPackage();
        splitData.index       = index;
        splitData.datas       = enddata;
        
        AddSplitData(splitData);

        reader = null;
    }

    public void AddSplitData(SplitPackage sData)
    {
        int sIndex = sData.index;
        if (!splitDataDic.containsKey(sIndex))
        {
            splitDataDic.put(sIndex, sData);
        }
        else
        {
        	MLog.Error(String.format("uniqueId %d messageId %d index %d ÷ÿ∏¥", uniqueId, Integer.toHexString(messageId), sIndex));
        }

    }

    public boolean IsReceiveFull()
    {
        if (splitDataDic.size() == 0)
        {
            return false;
        }

        return splitDataDic.size() == split;
    }

    public ProtocolStreamReader CombineData()
    {
        if (splitDataDic.size() == split)
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
            
            if (gzip == 1)
            {
                datas = SVZipUtils.ConvertBytesGZip(datas, SVZipUtils.CompressionMode.Decompress);
            }

            ProtocolStreamReader reader = new ProtocolStreamReader(datas);

            return reader;
        }

        return null;
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