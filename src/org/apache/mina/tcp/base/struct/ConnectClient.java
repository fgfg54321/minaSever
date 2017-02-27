package org.apache.mina.tcp.base.struct;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.stream.ProtocolStreamWriter;

public class ConnectClient
{
	public long              id;
	public String            token;
	public String            name;
	public Route             route;
	public Route             nextRoute;
	public IoSession         session;
	
	public ConnectClient()
	{
		
	}
	
	public void Write(ProtocolStreamWriter writer)
	{
		writer.WriteInt64(id);
		writer.WriteString16(token);
		writer.WriteString16(name);
		route.Write(writer);
	}
	
	public void Read(ProtocolStreamReader reader)
	{
		id       = reader.ReadInt64();
		token     = reader.ReadString16();
		name  = reader.ReadString16();
		nextRoute.Read(reader);
	}
}
