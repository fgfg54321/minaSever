package org.apache.mina.tcp.base.transserver.protocol.tick;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.TransServerManager;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class TServerReceiveTServerDSOffLineReader extends TCPBaseReader
{

	protected int type;
	protected long id;
	
	public int GetType()
	{
		return type;
	}
	
	public long GetId()
	{
		return id;
	}
	
	@Override
	public int GetSrcServerId()
    {
    	return TServerConfig.SERVER_ID;
    }
	
	@Override
	public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_OFFLINE;
    }
    
	@Override
    public  void ReadContent(ProtocolStreamReader reader)
    {
    }
    
	@Override
    public void OnReader(IoSession session,Object param)
    {
    	TransServerManager manager = (TransServerManager)param;
    	manager.LoginOut(session);
    }
    
}