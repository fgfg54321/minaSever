package org.apache.mina.tcp.base.logicserver.protocol.customer.client.login;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.logicserver.LogicConfig;
import org.apache.mina.tcp.base.logicserver.LogicServerManager;
import org.apache.mina.tcp.base.logicserver.protocol.customer.LogicBaseReader;
import org.apache.mina.tcp.base.struct.ConnectClient;
import org.apache.mina.tcp.base.utils.CProtocol;

@CProtocol(messageId = LogicConfig.MESSAGE_LOGIC_LOGIN,enable = true)
public class LogicClientLoginReader extends LogicBaseReader
{

	public String token;
	public long   uid;
	
	public long GetDstServerId()
    {
    	return LogicConfig.SERVER_ID;
    }
	
	public int GetMessageId()
    {
    	return LogicConfig.MESSAGE_LOGIC_LOGIN;
    }
    
    public  void ReadContent(ProtocolStreamReader reader)
    {
    	token    = reader.ReadString16();
    	uid      = reader.ReadInt64();
    }
    
    
    public void  OnReader(IoSession session,Object param)
    {
    	LogicServerManager manager = (LogicServerManager)param;
    	manager.ClientLogin((ConnectClient)connectInfo);
    }
    
    
}
