package org.apache.mina.gate.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class GateBaseCodecFactory implements ProtocolCodecFactory 
{
    private ProtocolGateBaseEncoder encoder;
    private ProtocolGateBaseDecoder decoder;

    public GateBaseCodecFactory() 
    {
        encoder = new ProtocolGateBaseEncoder();
        decoder = new ProtocolGateBaseDecoder();
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
