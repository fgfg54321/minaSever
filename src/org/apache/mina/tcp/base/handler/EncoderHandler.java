package org.apache.mina.tcp.base.handler;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;

public class EncoderHandler
{
	public boolean IsMeet(IoSession session)
	{
		return true;
	}
	
	public boolean Encode(TCPBaseWriter tcpWriter,IoSession session, ProtocolEncoderOutput output)
	{
		tcpWriter.WriteDirectly(session,output);
		return true;
	}
}
