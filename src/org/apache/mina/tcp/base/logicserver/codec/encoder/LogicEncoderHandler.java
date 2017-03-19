package org.apache.mina.tcp.base.logicserver.codec.encoder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.tcp.base.handler.EncoderHandler;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;

public class LogicEncoderHandler extends EncoderHandler
{
	
	@Override
	public boolean Encode(TCPBaseWriter tcpWriter,IoSession session, ProtocolEncoderOutput out)
	{
		
		
		tcpWriter.WriteDirectly(session, out);
			
		return true;
	}
	
	
}

