package org.apache.mina.tcp.base.struct;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.stream.ProtocolStreamWriter;

public class ConnectLServer extends ConnectBase
{
	public long      id;
	public String    token;
	public String    name;
	public Route     route;
	public Route     fromRoute;
	public IoSession session;
	
	public void Write(ProtocolStreamWriter writer)
	{
		writer.WriteInt64(id);
		writer.WriteString16(token);
		writer.WriteString16(name);
	}
	
	public void Read(ProtocolStreamReader reader)
	{
		id  = reader.ReadInt64();
		token        = reader.ReadString16();
		name   = reader.ReadString16();
	}
	
}
