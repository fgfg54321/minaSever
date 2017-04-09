package org.apache.mina.tcp.base.stream;

import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.utils.Action;

public class TCPBaseWriter
{
	public ProtocolStreamWriter writer = new ProtocolStreamWriter();

    public Action<TCPBaseWriter>  onRequestEvent;

    protected long    uniqueId;
    protected boolean isGZip;
    protected int     sliceIndex;
    protected int     sliceSize;
    
    public long GetUniqueId()
    {
    	return uniqueId;
    }
    
	public long GetSrcServerId()
    {
    	return 0;
    }
    
    public long GetDstServerId()
    {
    	return 0;
    }
    
    public int GetMessageId()
    {
    	return 0;
    }
    
    public boolean IsGZip()
    {
    	return isGZip;
    }
    
    public int GetSliceSize()
    {
    	return sliceSize;
    }
    
    public int GetSliceIndex()
    {
    	return sliceIndex;
    }
    
    protected void WriteHeader(ProtocolStreamWriter writer)
    {
    	writer.WriteInt64(GetSrcServerId());
    	writer.WriteInt64(GetDstServerId());
    	writer.WriteInt32(GetMessageId());
    	writer.WriteInt64(GetUniqueId());
    	writer.WriteBoolean(IsGZip());
    	writer.WriteInt32(GetSliceSize());
    	writer.WriteInt32(GetSliceIndex());
    }
    
    protected void WriteContent(ProtocolStreamWriter writer)
    {
    	
    }
    
    public void GZipOrSplitWrite(IoSession session, ProtocolEncoderOutput output)
    {
    	WriteContent(writer);
    	Send(session,output);
    }
    
    public void WriteDirectly(IoSession session,ProtocolEncoderOutput output)
    {
    	WriteContent(writer);
    	SendDirectly(session,output);
    }
    
    public List<IoBuffer> GenerateGZipOrSplitBuffer()
    {
    	WriteContent(writer);
    	
    	List<IoBuffer> sendBuffer       = new ArrayList<IoBuffer>();
    	
    	List<byte[]> datasList        = new ArrayList<byte[]>();
    	uniqueId                      = GetUniqueId();
    	isGZip                        = writer.GZipOrSplitBytes(datasList);
	    sliceSize                     = datasList.size();
	 
	    for(int i = 0 ; i < sliceSize; i++)
	    {
	        byte[] datas = datasList.get(i);
	
	        ProtocolStreamWriter sliceWriter = new ProtocolStreamWriter();
	        sliceIndex         = i;
	        WriteHeader(sliceWriter);
	        
	        sliceWriter.WriteBytes(datas);
	        int len           = sliceWriter.GetLength();
	        byte[] buffers    = sliceWriter.GetBuffer();
	        
	        IoBuffer ioBuffer = IoBuffer.allocate(len + 4);
	        ioBuffer.putInt(len);
	        ioBuffer.put(buffers);
	        ioBuffer.flip();
	        sendBuffer.add(ioBuffer);
	    }
	    
	    datasList.clear();
	    
	    return sendBuffer;
    }
 
    public void Send(IoSession session, ProtocolEncoderOutput output)
    {
    	ArrayList<byte[]> datasList        = new ArrayList<byte[]>();
    	uniqueId                           = GetUniqueId();
    	isGZip                             = writer.GZipOrSplitBytes(datasList);
	    sliceSize                          = datasList.size();
	 
	    for(int i = 0 ; i < sliceSize; i++)
	    {
	        byte[] datas = datasList.get(i);
	
	        ProtocolStreamWriter sliceWriter = new ProtocolStreamWriter();
	        sliceIndex = i;
	        WriteHeader(sliceWriter);
	        
	        sliceWriter.WriteBytes(datas);
	        int len   = sliceWriter.GetLength();
	
	        IoBuffer ioBuffer = IoBuffer.allocate(len + 4);
	        ioBuffer.putInt(len);
	        byte[] buffers    = sliceWriter.GetBuffer();
	        ioBuffer.put(buffers);
	        ioBuffer.flip();
	        
	        output.write(ioBuffer);
	    }
    }
    
    protected void SendDirectly(IoSession session,ProtocolEncoderOutput output)
    {
    	uniqueId                           = GetUniqueId();
    	isGZip                             = false;
	    sliceSize                          = 1;
	    sliceIndex                         = 0;
	    
        ProtocolStreamWriter sliceWriter = new ProtocolStreamWriter();
        
        WriteHeader(sliceWriter);
        byte[] datas = writer.GetBuffer();
        sliceWriter.WriteBytes(datas);
        
        int len           = sliceWriter.GetLength();
        IoBuffer ioBuffer = IoBuffer.allocate(len + 4);
        ioBuffer.putInt(len);
        byte[] buffers    = sliceWriter.GetBuffer();
        ioBuffer.put(buffers);
        ioBuffer.flip();
        
        output.write(ioBuffer);
    }
    
    public void  OnWriter(IoSession session,ProtocolEncoderOutput output,Object param)
    {
    	
    }

}
