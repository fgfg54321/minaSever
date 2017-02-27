package org.apache.mina.logic.chat.codec.server.tick;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.logic.chat.Config;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.TransServerManager;

public class ServerOffLineReader extends TCPBaseReader
{
	
	public enum OffLineType
	{
		SERVER(0),
		CLIENT(1);
		
		private int mType;
		OffLineType(int type)
		{
			mType = type;
		}
	}
	
    protected OffLineType type;
    public long id;
    

	public int GetMessageId()
    {
    	return Config.MESSAGE_OFFLINE;
    }
    
	
    public  void ReadContent(ProtocolStreamReader reader)
    {
    	int typev = reader.ReadInt32();
    	type      = OffLineType.valueOf(String.valueOf(typev));
    	id        = reader.ReadInt64();
    }
    
    public void OnReader(IoSession session,Object param)
    {
    	TransServerManager manager = (TransServerManager)param;
    	manager.LoginOut(session);
    }
    
}