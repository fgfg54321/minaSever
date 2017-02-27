package org.apache.mina.tcp.base.struct;

import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.stream.ProtocolStreamWriter;

public class Route
{
	public int serverId;
	
	public void Write(ProtocolStreamWriter writer)
	{
		writer.WriteInt32(serverId);
	}
	
	public void Read(ProtocolStreamReader reader)
	{
		serverId  = reader.ReadInt32();
	}
}
