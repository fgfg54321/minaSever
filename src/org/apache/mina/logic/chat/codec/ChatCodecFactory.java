package org.apache.mina.logic.chat.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.logic.chat.codec.decoder.ProtocolBaseDecoder;
import org.apache.mina.logic.chat.codec.encoder.ProtocolBaseEncoder;

public class ChatCodecFactory implements ProtocolCodecFactory 
{
    private ProtocolBaseEncoder encoder;
    private ProtocolBaseDecoder decoder;

    public ChatCodecFactory() 
    {
        encoder = new ProtocolBaseEncoder();
        decoder = new ProtocolBaseDecoder();
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
