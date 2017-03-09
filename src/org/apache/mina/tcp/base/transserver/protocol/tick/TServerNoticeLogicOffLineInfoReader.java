package org.apache.mina.tcp.base.transserver.protocol.tick;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.transserver.TServerConfig;
import org.apache.mina.tcp.base.transserver.TransServerManager;

public class TServerNoticeLogicOffLineInfoReader extends TCPBaseReader
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
	public long GetSrcServerId()
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
    	type = reader.ReadInt32();
    	id   = reader.ReadInt64();
    }
    
	@Override
    public void OnReader(IoSession session,Object param)
    {
    	TransServerManager manager = (TransServerManager)param;
    	if(type == TServerConfig.TYPE_CLIENT)
    	{
    		manager.ClientLoginOut(id);
    	}
    	else if(type == TServerConfig.TYPE_LSERVER)
    	{
    		manager.LServerLoginOut(id);
    	}
    	else if(type == TServerConfig.TYPE_TSERVER)
    	{
    		manager.TServerLoginOut(id);
    	}
    }
    
}