package org.apache.mina.tcp.base.client.codec.encoder;

import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;

public class ProtocolClientTransEncoder extends ProtocolEncoderAdapter 
{
	public List<EncoderHandler> hanlderList = new ArrayList<EncoderHandler>();
	
	public ProtocolClientTransEncoder()
	{
		hanlderList.add(new ClientEncoderHandler());
	}
	
	@Override
	public void encode(IoSession session, Object obj, ProtocolEncoderOutput output) throws Exception
	{
		TCPBaseWriter request   = (TCPBaseWriter)obj;
		
		 for(int i = 0; i < hanlderList.size();i++)
         {
        	EncoderHandler handler = hanlderList.get(i);
        	if(handler.IsMeet(session))
        	{
        		handler.Encode(request, session);
        	}
         }
	}
	
}
