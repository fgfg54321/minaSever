package org.apache.mina.tcp.base.client.codec.encoder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.tcp.base.handler.EncoderHandler;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;

public class ClientEncoderHandler extends EncoderHandler
{
	public boolean Encode(TCPBaseWriter tcpWriter,IoSession session, ProtocolEncoderOutput output)
	{
		
		tcpWriter.GZipOrSplitWrite(session,output);
		
		return true;
	}
}

