package org.apache.mina.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.mina.utils.SVZipUtils;
import org.apache.mina.utils.SVZipUtils.CompressionMode;

public class ProtocolStreamWriter
{
	public static int GZIP_LIMITSIZE  = 1024;
    public static int SPLIT_LIMITSIZE = 2 * 100;

	private ByteArrayOutputStream writer;

    public ProtocolStreamWriter()
	{
		writer  = new ByteArrayOutputStream(); 
	}
    
	public void WriteByte(byte value)
	{
		writer.write(value);
	} 

	public void WriteBytes(byte[] bytes)
	{
		try
		{
			writer.write(bytes);
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void WriteInt16(short value)
	{
		try
		{
			writer.write
			(
				ByteConvert.shortToBytes(value)
			);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void WriteInt32(int value)
	{
		try
		{
			writer.write
			(
				ByteConvert.intToBytes(value)
			);
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void WriteInt64(long value)
	{
		try
		{
			this.writer.write
			(
				ByteConvert.longToBytes(value)
			);
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void WriteString16(String value)
	{
		try
		{
	        byte [] byteArray = value.getBytes("UTF8");
	
			WriteInt16
			(
	            (short) byteArray.length
			);
			
			writer.write
			(
				byteArray
			);
			
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void WriteBoolean(boolean value)
	{
		byte b = (byte)(value ? 1:0);
		writer.write(b);
	}

//////////////////////////////////////////////////////////////////////////

	public byte[] GetBuffer()
	{
		return writer.toByteArray();
	}

    public int GetLength()
	{
        return writer.size();
	}
    

    public void Reset()
    {
    	writer.reset();
    }


    public boolean GZipOrSplitBytes(List<byte[]> datasList)
    { 
        boolean gzip              = false;
        byte[] datas              = GetBuffer();
        if (datas.length > GZIP_LIMITSIZE)
        {
            byte[] gzipDatas   = SVZipUtils.ConvertBytesGZip(datas, CompressionMode.Compress);
            gzip               = true;

            if (gzipDatas.length > SPLIT_LIMITSIZE)
            {
                int packageSize    = (int)(gzipDatas.length / SPLIT_LIMITSIZE);
                int lastSize       = (int)(gzipDatas.length % SPLIT_LIMITSIZE);
               
                if(lastSize > 0)
                {
                    packageSize = packageSize + 1;
                }

                int offest = 0;
                for(int i = 0; i < packageSize; i++)
                {
                    int copyLen = SPLIT_LIMITSIZE;
                    if(lastSize > 0 && 
                       i == packageSize - 1)
                    {
                        copyLen = lastSize;
                    }

                    byte[] splitData = new byte[copyLen];
                    System.arraycopy(gzipDatas, offest, splitData, 0, copyLen);
                    datasList.add(splitData);

                    offest += copyLen;
                }
            }
            else
            {
                datasList.add(gzipDatas);
            }
        }
        else
        {
            
            gzip   = false;
            datasList.add(datas);
        }

        return gzip;
    }
    

    public void Close()
    {
        try
		{
			writer.close();
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
