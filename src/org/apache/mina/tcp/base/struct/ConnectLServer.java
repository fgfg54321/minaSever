package org.apache.mina.tcp.base.struct;

import org.apache.mina.tcp.base.transserver.TServerConfig;

public class ConnectLServer extends ConnectBase
{
	public Route             route;
	public Route             fromRoute;
	
	public ConnectLServer()
	{
		type = TServerConfig.TYPE_LSERVER;
	}
	
	
}
