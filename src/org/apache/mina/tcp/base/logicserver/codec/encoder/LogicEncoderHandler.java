package org.apache.mina.tcp.base.logicserver.codec.encoder;

import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.tcp.base.handler.EncoderHandler;
import org.apache.mina.tcp.base.logicserver.protocol.customer.LogicBaseWriter;
import org.apache.mina.tcp.base.logicserver.protocol.transmission.LogicToTServerTransWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;

public class LogicEncoderHandler extends EncoderHandler
{
	
	@Override
	public boolean Encode(TCPBaseWriter tcpWriter,IoSession session, ProtocolEncoderOutput out)
	{
		
			if(tcpWriter instanceof LogicBaseWriter)
			{
				LogicBaseWriter logicBaseWrite = (LogicBaseWriter)tcpWriter;
		
				List<IoBuffer> sendBuffers    = logicBaseWrite.GenerateSendBuffer();
				for(int i = 0; i < sendBuffers.size();i++)
				{
					IoBuffer buffer = sendBuffers.get(i);
					out.write(buffer);
				}
			}
			else
			{
				tcpWriter.WriteDirectly(session, out);
			}
		
		
		return true;
	}
	
	
}

