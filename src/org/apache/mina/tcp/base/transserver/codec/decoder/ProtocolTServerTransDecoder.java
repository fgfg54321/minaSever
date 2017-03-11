package org.apache.mina.tcp.base.transserver.codec.decoder;

import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.log.MLog;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.handler.DecoderHandler;

public class ProtocolTServerTransDecoder extends CumulativeProtocolDecoder
{
	public List<DecoderHandler> hanlderList = new ArrayList<DecoderHandler>();

	public ProtocolTServerTransDecoder()
	{
		hanlderList.add(new ClientDecoderHandler());
		hanlderList.add(new LogicServerDecoderHandler());
		hanlderList.add(new TransServerDecoderHandler());
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
			        		handler.Decode(reader,session,out);
			        	}
			        }
			        
			        return true;
			  	}
			  	
			  }
			  
			 return false;
		 }
		 catch(Exception e)
		 {
			e.printStackTrace();
		 }
	 return true;
  }
}
