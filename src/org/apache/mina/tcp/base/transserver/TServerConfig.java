package org.apache.mina.tcp.base.transserver;

public class TServerConfig
{
	public static final int CLIENT_ID       = 0xffffffff;
	public static final int SERVER_ID       = 0x00000000;
	
	//Connect Server
	public static final int MESSAGE_LOGIN   = 0x000000000;
	public static final int MESSAGE_OFFLINE = 0x000000001;
	public static final int MESSAGE_TRANS   = 0x000000002;
	
	public static final int TYPE_CLIENT      = 0;
	public static final int TYPE_LSERVER     = 1;
	public static final int TYPE_TSERVER     = 2;
}
