package org.apache.mina.tcp.base.logicserver.codec.encoder;

import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.tcp.base.handler.EncoderHandler;
import org.apache.mina.tcp.base.stream.TCPBaseWriter;

public class ProtocolLogicTransEncoder extends ProtocolEncoderAdapter 
{
	public List<EncoderHandler> hanlderList = new ArrayList<EncoderHandler>();
	
	public ProtocolLogicTransEncoder()
	{
		hanlderList.add(new LogicEncoderHandler());
	}
	
	@Override
	public void encode(IoSession session, Object obj, ProtocolEncoderOutput output) throws Exception
	{
		try
		{
			TCPBaseWriter request   = (TCPBaseWriter)obj;
			
			 for(int i = 0; i < hanlderList.size();i++)
	         {
	        	EncoderHandler handler = hanlderList.get(i);
	        	if(handler.IsMeet(session))
	        	{
	        		
	        			handler.Encode(request, session,output);
	        		
	        	}
	         }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
