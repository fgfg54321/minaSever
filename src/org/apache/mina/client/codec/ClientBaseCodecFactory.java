package org.apache.mina.client.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class ClientBaseCodecFactory implements ProtocolCodecFactory 
{
    private ProtocolClientBaseEncoder encoder;
    private ProtocolClientBaseDecoder decoder;

    public ClientBaseCodecFactory() 
    {
        encoder = new ProtocolClientBaseEncoder();
        decoder = new ProtocolClientBaseDecoder();
    }

    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception
{
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception 
    {
        return decoder;
    }
}
