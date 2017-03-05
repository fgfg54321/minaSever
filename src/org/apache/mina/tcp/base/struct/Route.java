package org.apache.mina.tcp.base.struct;

import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.stream.ProtocolStreamWriter;

public class Route
{
	public long id;
	
	public Route()
	{
		
	}
	
	public Route(long id)
	{
		this.id = id;
	}
	
	public void Write(ProtocolStreamWriter writer)
	{
		writer.WriteInt64(id);
	}
	
	public void Read(ProtocolStreamReader reader)
	{
		id  = reader.ReadInt64();
	}
}
