package org.apache.mina.tcp.base.logicserver.codec.decoder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;

public class DecoderHandler
{
	
	public boolean IsMeet(IoSession session)
	{
		return true;
	}
	
	public boolean Decode(ProtocolStreamReader reader,IoSession session)
	{
		return true;
	}
}
