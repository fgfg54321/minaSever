package org.apache.mina.tcp.base.transserver.codec.encoder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.tcp.base.transserver.TransServerManager;

public class TransServerEncoderHandler extends EncoderHandler
{
	public boolean IsMeet(IoSession session)
	{
		boolean isTServer = TransServerManager.IsTServer(session);
		return isTServer;
	}
}

