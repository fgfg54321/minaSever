package org.apache.mina.tcp.base.transserver.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.tcp.base.transserver.codec.decoder.ProtocolTServerTransDecoder;
import org.apache.mina.tcp.base.transserver.codec.encoder.ProtocolTServerTransEncoder;

public class TServerCodecFactory implements ProtocolCodecFactory 
{
    private ProtocolTServerTransEncoder encoder = new ProtocolTServerTransEncoder();
    private ProtocolTServerTransDecoder decoder = new ProtocolTServerTransDecoder();
    
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception
    {
    	return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception 
    {
    	return decoder;
    }
}
