package org.apache.mina.tcp.base.logicserver;

public class LogicConfig
{
	//Connect Server
	public static final int CONNECT_SERVER_ID       = 0x00000000;
	
	public static final int MESSAGE_CONNECT_LOGIN   = 0x00000000;
	public static final int MESSAGE_CONNECT_OFFLINE = 0x00000001;
	public static final int MESSAGE_CONNECT_TRANS   = 0x000000002;
	
	public static final int SERVER_ID               = 0x10000000;
	//Logic Server
	public static final int MESSAGE_LOGIC_LOGIN     = 0x00000000;
	
	public static final int TYPE_CLIENT             = 0;
	public static final int TYPE_LSERVER             = 1;
	public static final int TYPE_TSERVER             = 1;
	
}
