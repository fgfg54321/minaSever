package org.apache.mina.tcp.base.transserver.codec.encoder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.tcp.base.handler.EncoderHandler;
import org.apache.mina.tcp.base.transserver.TransServerManager;

public class ClientEncoderHandler extends EncoderHandler
{
	public boolean IsMeet(IoSession session)
	{
		boolean isClient = TransServerManager.IsClient(session);
		return isClient;
	}
}
