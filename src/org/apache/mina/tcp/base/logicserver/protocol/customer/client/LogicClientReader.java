package org.apache.mina.tcp.base.logicserver.protocol.customer.client;

import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.ConnectClient;

public class LogicClientReader extends TCPBaseReader
{
	public ConnectClient connectClient;
	
	public void SetClientInfo(ConnectClient connectClient)
	{
		this.connectClient = connectClient;
	}
}
