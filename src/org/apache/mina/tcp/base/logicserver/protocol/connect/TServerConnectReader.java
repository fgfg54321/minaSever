package org.apache.mina.tcp.base.logicserver.protocol.connect;

import org.apache.mina.client.Config;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.logicserver.LogicServerManager;
import org.apache.mina.tcp.base.logicserver.protocol.customer.LogicBaseReader;
import org.apache.mina.tcp.base.struct.ConnectBase;
import org.apache.mina.tcp.base.struct.ConnectTServer;

public class TServerConnectReader extends LogicBaseReader
{
	public boolean result;
    public int     code;
    public String  message;
    
	public int GetMessageId()
    {
    	return Config.MESSAGE_LOGIN;
    }
    
    public  void ReadContent(ProtocolStreamReader reader)
    {
    	connectInfo = ConnectBase.ConnectFactory(reader);
    	result      = reader.ReadBoolean();
    	code        = reader.ReadInt32();
    	message     = reader.ReadString16();
    }
    
    public void  OnReader(IoSession session,Object param)
    {
    	LogicServerManager manager = (LogicServerManager)param;
    	manager.ConnectTServer(session,(ConnectTServer)connectInfo);
    }
}

