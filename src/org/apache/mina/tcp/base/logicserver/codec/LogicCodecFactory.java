package org.apache.mina.tcp.base.logicserver.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.tcp.base.logicserver.codec.decoder.ProtocolLogicTransDecoder;
import org.apache.mina.tcp.base.logicserver.codec.encoder.ProtocolLogicTransEncoder;

public class LogicCodecFactory implements ProtocolCodecFactory 
{
    private ProtocolLogicTransEncoder encoder = new ProtocolLogicTransEncoder();
    private ProtocolLogicTransDecoder decoder = new ProtocolLogicTransDecoder();
    
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception
    {
    	return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception 
    {
    	return decoder;
    }
}
