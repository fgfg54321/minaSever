package org.apache.mina.tcp.base.client;

public class ClientConfig
{
	//Connect Server
	public static final int CONNECT_SERVER_ID       = 0x00000000;
	
	public static final int CONNECT_MESSAGE_LOGIN   = 0x00000000;
	public static final int CONNECT_MESSAGE_OFFLINE = 0x00000001;
	
	
	public static final int LOGIC_SERVER_ID         = 0x10000000;
	//Logic Server
	public static final int LOGIC_MESSAGE_LOGIN     = 0x00000000;
	
	public static final int TYPE_CLIENT             = 0;
	public static final int TYPE_SERVER             = 1;
}
