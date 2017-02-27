package org.apache.mina.tcp.base.transserver.protocol.transmission;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.ConnectClient;
import org.apache.mina.tcp.base.struct.ConnectLServer;
import org.apache.mina.tcp.base.struct.ConnectTServer;
import org.apache.mina.tcp.base.struct.TransServerManager;
import org.apache.mina.tcp.base.transserver.TServerConfig;

public class TServerToTServerTransReader extends TCPBaseReader
{

	public ConnectClient  client  = new ConnectClient();
	public ConnectLServer lServer  = new ConnectLServer();
	public int    type;
    public byte[] datas;

    
    @Override
    public int GetSrcServerId()
    {
    	return TServerConfig.SERVER_ID;
    }
    
    @Override
    public int GetMessageId()
    {
    	return TServerConfig.MESSAGE_TRANS;
    }
    
    public long GetId()
    {
    	if(type == TServerConfig.TYPE_CLIENT)
    	{
    		return client.id;
    	}
    	else if(type == TServerConfig.TYPE_LSERVER)
    	{
    		return lServer.id;
    	}
    	
    	return 0;
    }
    
    
    public  void ReadContent(ProtocolStreamReader reader)
    {

    	type = reader.ReadInt32();
    	
    	if(type == TServerConfig.TYPE_CLIENT)
    	{
    	 	client.Read(reader);
    	}
    	else if(type == TServerConfig.TYPE_LSERVER)
    	{
    		lServer.Read(reader);
    	}
    	datas = reader.ReadToEnd();
    	
    }
    
    public void OnReader(IoSession session,Object param)
    {
    	TransServerManager manager       = (TransServerManager)param;
    	if(type == TServerConfig.TYPE_CLIENT)
    	{
    		long id                   = GetId();
        	ConnectClient curClient      = manager.GetClient(id);
        	if(curClient != null && curClient.nextRoute != null)
        	{
        		int nextServerId = curClient.nextRoute.serverId;
        		if(nextServerId == TServerConfig.SERVER_ID)
        		{
            		TServerToClientTransWriter cQueryResponse = new TServerToClientTransWriter(datas);
            		curClient.session.write(cQueryResponse);
        		}
        		else//post next transServer
        		{
        			ConnectTServer tServer = manager.GetTServer(nextServerId);
        			TServerToTServerTransWriter tServerWriter = new TServerToTServerTransWriter(curClient,datas);
        			tServer.session.write(tServerWriter);
        			
        		}
        		
        	}
        	
    	}
    	else if(type == TServerConfig.TYPE_LSERVER)
    	{
    		long id                    = GetId();
    		ConnectLServer curServer   = manager.GetLServer(id);
    		if(curServer != null &&  curServer.nextRoute != null)
    		{
    			long nextServerId = curServer.nextRoute.serverId;
    			if(nextServerId == TServerConfig.SERVER_ID)
    			{
    				TServerToLogicTransWriter cQueryResponse = new TServerToLogicTransWriter(curServer,datas);
        			curServer.session.write(cQueryResponse);	
    			}
    			else//post next transServer
    			{
    				ConnectTServer tServer = manager.GetTServer(nextServerId);
        			TServerToTServerTransWriter tServerWriter = new TServerToTServerTransWriter(curServer,datas);
        			tServer.session.write(tServerWriter);
    			}
    							
    		}	
    	}
    }
}
