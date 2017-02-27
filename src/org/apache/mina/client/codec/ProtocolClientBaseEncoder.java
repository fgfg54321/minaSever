package org.apache.mina.client.codec;

import java.util.ArrayList;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.stream.ProtocolStreamWriter;


public class ProtocolClientBaseEncoder extends ProtocolEncoderAdapter 
{
	@Override
	public void encode(IoSession session, Object obj, ProtocolEncoderOutput output) throws Exception
	{
		ClientBaseResponse response = (ClientBaseResponse)obj;
		response.BaseSerialize();
        response.Serialize();

        ArrayList<IoBuffer> sendBufferList = GzipOrSplitToSendQueue(response);
        for(int i = 0; i < sendBufferList.size(); i++)
        {
        	IoBuffer ioBuffer = sendBufferList.get(i);
            session.write(ioBuffer);
        }
        
        response.OnSent();
	}
	
	
	protected ArrayList<IoBuffer> GzipOrSplitToSendQueue(ClientBaseResponse response)
	{
		ArrayList<IoBuffer> sendBufferList = new ArrayList<IoBuffer>();
		ArrayList<byte[]> datasList        = new ArrayList<byte[]>();
	    ProtocolStreamWriter writer        = response.writer;
	    long uniqueId                      = response.GetUniqueId();
	    int srcServerId                    = response.GetSrcServerId();
	    int messageId                      = response.GetMessageId();
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
