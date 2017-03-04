package org.apache.mina.tcp.base.logicserver.protocol.customer;

import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.struct.ConnectBase;

public class LogicBaseWriter extends TCPBaseWriter
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
