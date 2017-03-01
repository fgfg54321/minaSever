package org.apache.mina.tcp.base.struct;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class ConnectBase
{
	public long              id;
	public String            token;
	public String            name;
	public Route             route;
	public Route             fromRoute;
	public IoSession         session;
	public int               type;
	
	public void Write(ProtocolStreamWriter writer)
	{
		writer.WriteInt32(type);
		writer.WriteInt64(id);
		writer.WriteString16(token);
		writer.WriteString16(name);
		route.Write(writer);
	}
	
	protected void Read(ProtocolStreamReader reader)
	{
		id       = reader.ReadInt64();
		token    = reader.ReadString16();
		name     = reader.ReadString16();
		fromRoute.Read(reader);
	}
	
	public static ConnectBase ConnectFactory(ProtocolStreamReader reader)
	{
		ConnectBase connectBase = null;
		int type = reader.ReadInt32();
		
		switch(type)
		{
			case TServerConfig.TYPE_CLIENT:
			{
				connectBase = new ConnectClient();
				break;
			}
			case TServerConfig.TYPE_LSERVER:
			{
				connectBase = new ConnectLServer();
				break;
			}
			case TServerConfig.TYPE_TSERVER:
			{
				connectBase = new ConnectTServer();
				break;
			}
		}
		
		if(connectBase != null)
		{
			connectBase.Read(reader);
		}
		return connectBase;
	}
}
