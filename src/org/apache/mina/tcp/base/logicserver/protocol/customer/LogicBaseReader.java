package org.apache.mina.tcp.base.logicserver.protocol.customer;

import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.ConnectBase;

public class LogicBaseReader extends TCPBaseReader
{
	protected ConnectBase connectInfo;
	
	
	public ConnectBase GetConnectInfo()
	{
		return connectInfo;
	}
	
	public void SetConnectInfo(ConnectBase connectBase)
	{
		this.connectInfo = connectBase;
	}
}
