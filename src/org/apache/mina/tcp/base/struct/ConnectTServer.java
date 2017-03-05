package org.apache.mina.tcp.base.struct;

import org.apache.mina.stream.ProtocolStreamReader;

public class ConnectTServer extends ConnectBase
{
	public boolean   enableTrans;
	
	public void Read(ProtocolStreamReader reader)
	{
		type     = reader.ReadInt32();
		id       = reader.ReadInt64();
		token    = reader.ReadString16();
		name     = reader.ReadString16();
	}
	
}
