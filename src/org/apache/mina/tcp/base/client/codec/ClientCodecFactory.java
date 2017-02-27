package org.apache.mina.tcp.base.client.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.tcp.base.client.codec.decoder.ProtocolClientTransDecoder;
import org.apache.mina.tcp.base.client.codec.encoder.ProtocolClientTransEncoder;

public class ClientCodecFactory implements ProtocolCodecFactory 
{
    private ProtocolClientTransEncoder encoder = new ProtocolClientTransEncoder();
    private ProtocolClientTransDecoder decoder = new ProtocolClientTransDecoder();
    
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception
    {
    	return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception 
    {
    	return decoder;
    }
}
