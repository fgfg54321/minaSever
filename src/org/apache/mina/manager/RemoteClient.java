package org.apache.mina.manager;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

public class RemoteClient
{
	public long      sessionId;
	public long      lastReceiveTime;
	public IoSession session;
	
	public void Receive(IoBuffer message)
	{
		long received = message.getLong();
		
	}
	
	public void Send()
	{
		
	}
}
