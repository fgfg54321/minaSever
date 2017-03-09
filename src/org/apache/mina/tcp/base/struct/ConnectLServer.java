package org.apache.mina.tcp.base.struct;

import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class ConnectLServer extends ConnectBase
{
	public ConnectLServer()
	{
		type = TServerConfig.TYPE_LSERVER;
	}
	
	public void Read(ProtocolStreamReader reader)
	{
		type     = reader.ReadInt32();
		id       = reader.ReadInt64();
		token    = reader.ReadString16();
		name     = reader.ReadString16();
	}
	
}
