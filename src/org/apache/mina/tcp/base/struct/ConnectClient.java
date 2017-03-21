package org.apache.mina.tcp.base.struct;

import org.apache.mina.tcp.base.transserver.TServerConfig;

public class ConnectClient extends ConnectBase
{

	public Route             route;
	public Route             fromRoute;
	
	public ConnectClient()
	{
		type = TServerConfig.TYPE_CLIENT;
	}
	
}
