package org.apache.mina.gate.codec;

import java.util.ArrayList;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.stream.ProtocolStreamWriter;


public class ProtocolGateBaseEncoder extends ProtocolEncoderAdapter 
{
	@Override
	public void encode(IoSession session, Object obj, ProtocolEncoderOutput output) throws Exception
	{
		GateBaseResponse request = (GateBaseResponse)obj;
		request.BaseSerialize();
        request.Serialize();

        ArrayList<IoBuffer> sendBufferList = GzipOrSplitToSendQueue(request);
        for(int i = 0; i < sendBufferList.size(); i++)
        {
        	IoBuffer ioBuffer = sendBufferList.get(i);
            session.write(ioBuffer);
        }
        
        request.OnSent();
	}
	
	
	protected ArrayList<IoBuffer> GzipOrSplitToSendQueue(GateBaseResponse request)
	{
		ArrayList<IoBuffer> sendBufferList = new ArrayList<IoBuffer>();
		ArrayList<byte[]> datasList        = new ArrayList<byte[]>();
	    ProtocolStreamWriter writer        = request.writer;
	    long uniqueId                      = request.GetUniqueId();
	    int srcServerId                    = request.GetSrcServerId();
	    int messageId                      = request.GetMessageId();
	    boolean gzip                       = writer.GZipOrSplitBytes(datasList);
	    int  split                         = datasList.size();
	
	    for(int i = 0 ; i < datasList.size(); i++)
	    {
	        byte[] datas = datasList.get(i);
	
	        ProtocolStreamWriter splitWriter = new ProtocolStreamWriter();
	        
	        splitWriter.WriteInt64(uniqueId);
	        splitWriter.WriteInt32(srcServerId);
	        splitWriter.WriteInt32(messageId);
	        splitWriter.WriteByte ((byte)(gzip? 1:0));
	        splitWriter.WriteInt32(split);
	        splitWriter.WriteInt32(i);
	        splitWriter.WriteBytes(datas);
	        int len   = splitWriter.GetLength();
	
	        IoBuffer ioBuffer = IoBuffer.allocate(len + 4);
	        ioBuffer.putInt(len);
	        byte[] buffers    = splitWriter.GetBuffer();
	        ioBuffer.put(buffers);
	        ioBuffer.flip();
	
	        sendBufferList.add(ioBuffer);
	    }
	    
        return sendBufferList;
	}
	
}
