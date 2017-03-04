package org.apache.mina.tcp.base.logicserver.codec.encoder;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.tcp.base.logicserver.protocol.customer.LogicBaseWriter;
import org.apache.mina.tcp.base.logicserver.protocol.transmission.LogicToTServerTransWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;

public class LogicEncoderHandler extends EncoderHandler
{

	public boolean Encode(TCPBaseWriter tcpWriter,IoSession session)
	{
		LogicBaseWriter logicBaseWrite = (LogicBaseWriter)tcpWriter;

		List<byte[]> sendBuffer        = logicBaseWrite.GenerateSendBuffer();
		for(int i = 0; i < sendBuffer.size();i++)
		{
			byte[] datas = sendBuffer.get(i);
			LogicToTServerTransWriter transWriter = new LogicToTServerTransWriter(logicBaseWrite.GetConnectInfo(),datas);
			transWriter.WriteDirectly(session);
		}
		
		return true;
	}
	
	
}

