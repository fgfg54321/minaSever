package org.apache.mina.tcp.base.logicserver.codec.encoder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;

public class EncoderHandler
{
	public boolean IsMeet(IoSession session)
	{
		return true;
	}
	
	public boolean Encode(TCPBaseWriter tcpWriter,IoSession session)
	{
		tcpWriter.Write(session);
		return true;
	}
}
