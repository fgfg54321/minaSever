package org.apache.mina.tcp.base.logicserver.codec.decoder;

import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.stream.ProtocolStreamReader;

public class ProtocolLogicTransDecoder extends CumulativeProtocolDecoder
{
	public List<DecoderHandler> hanlderList = new ArrayList<DecoderHandler>();

	public ProtocolLogicTransDecoder()
	{
		hanlderList.add(new LogicDecoderHandler());
	}
	
	@Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception
    {
	  int available = in.remaining();
	  if(available > 4)
	  {
	  	int len = in.getInt();
	  	if(len > in.remaining())
	  	{
	  		byte[] dataBytes                = new byte[len];
	        in.get(dataBytes);
	        ProtocolStreamReader reader     = new ProtocolStreamReader(dataBytes);
	        for(int i = 0; i < hanlderList.size();i++)
	        {
	        	DecoderHandler handler = hanlderList.get(i);
	        	if(handler.IsMeet(session))
	        	{
	        		handler.Decode(reader, session);
	        	}
	        }
	        
	        return true;
	  	}
	  }
	
	return false;
  }
}
