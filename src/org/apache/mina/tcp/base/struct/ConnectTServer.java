package org.apache.mina.tcp.base.struct;

import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class ConnectTServer extends ConnectBase
{
	public boolean   enableTrans;
	
	public ConnectTServer()
	{
		type = TServerConfig.TYPE_TSERVER;
	}
	
	
}
