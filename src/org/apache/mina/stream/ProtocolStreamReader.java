package org.apache.mina.stream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ProtocolStreamReader
{
	public static int				GZIP_LIMITSIZE	= 1024;
	public static int				SPLIT_LIMITSIZE	= 2 * 100;

	private ByteArrayInputStream	reader;
	private int position      = 0;
	private int markPosition  = 0;

	public ProtocolStreamReader(byte[] buffer)
	{
		reader = new ByteArrayInputStream(buffer);
	}

	// ///////////////////////////////////////////////////////////////////////////////
	// Reader
	// ///////////////////////////////////////////////////////////////////////////////

	public long ReadInt64()
	{
		byte[] data = new byte[8];
		reader.read(data, 0, data.length);
		position += 8;
		return ByteConvert.bytesToLong(data);
	}

	public int ReadInt32()
	{
		byte[] data = new byte[4];
		reader.read(data, 0, data.length);
		position+=4;
		return ByteConvert.bytesToInt(data);
	}

	public short ReadInt16()
	{
		byte[] data = new byte[2];
		reader.read(data, 0, data.length);
		position    +=2;
		return ByteConvert.bytesToShort(data);
	}

	public byte ReadByte()
	{
		byte data = (byte) reader.read();
		position  +=1;
		return data;
	}

	public int ReadBytes(byte[] buffer, int index, int count)
	{
		int rCount = reader.read(buffer, index, count);
		position  +=rCount;
		return rCount;
	}

	public String ReadString16()
	{
		String res = null;
		try
		{
			int len       = ReadInt16();
			byte[] buffer = new byte[len];
			int rCount    = reader.read(buffer, 0, len);
			res = new String(buffer, "UTF-8");
			position     += rCount;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return res;
	}
	
	public boolean ReadBoolean()
	{
		int b =  reader.read();
		return (b == 1);
	}
	
	public void Mark()
	{
		reader.mark(0);
		markPosition = position;
	}
	
	public void Reset()
	{
		reader.reset();
		position = markPosition;
	}

	public byte[] ReadToEnd()
	{
		int len     = reader.available();
		byte[] data = new byte[len];
		int rCount  = reader.read(data, 0, (int) len);
		position   += rCount;
		return data;

	}

	// ////////////////////////////////////////////////////////////////////////

	public byte[] GetBuffer()
	{
		return ReadToEnd();
	}

	public int GetPosition()
	{
		return (int) position;
	}

	public int GetLength()
	{
		return (int) reader.available();
	}

	public void Close()
	{
		try
		{
			reader.close();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
