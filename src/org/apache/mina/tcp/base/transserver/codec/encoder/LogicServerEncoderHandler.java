package org.apache.mina.tcp.base.transserver.codec.encoder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.tcp.base.struct.TransServerManager;

public class LogicServerEncoderHandler extends EncoderHandler
{
	public boolean IsMeet(IoSession session)
	{
		boolean isServer = TransServerManager.IsLServer(session);
		return isServer;
	}
}

