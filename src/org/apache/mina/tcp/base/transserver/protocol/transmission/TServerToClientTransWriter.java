package org.apache.mina.tcp.base.transserver.protocol.transmission;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class TServerToClientTransWriter extends TCPBaseWriter
{
    public byte[] datas;
    
    public TServerToClientTransWriter(byte[] datas)
    {
    	this.datas = datas;
    }
    
    @Override
    public long GetSrcServerId()
    {
    	return TServerConfig.SERVER_ID;
    }
    
    @Override
    public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_TRANS;
    }

    protected void WriteHeader(ProtocolStreamWriter writer)
    {
    	
    }
    
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	writer.WriteBytes(datas);
    }
    
    /*
     * Override WriteDirectly use Logic data to Client without any header/length
     * 
     */
    @Override
	public void WriteDirectly(IoSession session,ProtocolEncoderOutput output)
    {
    	
        IoBuffer ioBuffer = IoBuffer.allocate(datas.length);
        ioBuffer.put(datas);
        ioBuffer.flip();
        
        output.write(ioBuffer);
    }
	 
}
