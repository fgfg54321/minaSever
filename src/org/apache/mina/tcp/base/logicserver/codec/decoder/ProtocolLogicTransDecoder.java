package org.apache.mina.tcp.base.logicserver.codec.decoder;

import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.handler.DecoderHandler;

public class ProtocolLogicTransDecoder extends CumulativeProtocolDecoder
{
	public List<DecoderHandler> hanlderList = new ArrayList<DecoderHandler>();

	public ProtocolLogicTransDecoder()
	{
		hanlderList.add(new LogicDecoderHandler());
	}
	
	@Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
    {
		try
		{
		  int available = in.remaining();
		  if(available > 4)
		  {
		  	int len = in.getInt();
		  	if(len >= available - 4)
		  	{
		  		byte[] dataBytes                = new byte[len];
		        in.get(dataBytes);
		        ProtocolStreamReader reader     = new ProtocolStreamReader(dataBytes);
		        for(int i = 0; i < hanlderList.size();i++)
		        {
		        	DecoderHandler handler = hanlderList.get(i);
		        	if(handler.IsMeet(session))
		        	{
		        		
		        			handler.Decode(reader, session,out);
		        	
		        	}
		        }
		        
		        return true;
		  	}
		  }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
	return false;
  }
}
