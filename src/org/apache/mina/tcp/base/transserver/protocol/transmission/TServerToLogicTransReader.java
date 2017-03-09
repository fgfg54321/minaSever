package org.apache.mina.tcp.base.transserver.protocol.transmission;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.ConnectBase;
import org.apache.mina.tcp.base.struct.ConnectClient;
import org.apache.mina.tcp.base.struct.ConnectLServer;
import org.apache.mina.tcp.base.struct.ConnectTServer;
import org.apache.mina.tcp.base.transserver.TServerConfig;
import org.apache.mina.tcp.base.transserver.TransServerManager;

public class TServerToLogicTransReader extends TCPBaseReader
{

	public ConnectBase   connectBase;
    public byte[] datas;

    
    @Override
    public long GetSrcServerId()
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
    	if(connectBase != null)
    	{
    		return connectBase.id;	
    	}
    	
    	return 0;
    }
    
    
    public  void ReadContent(ProtocolStreamReader reader)
    {

    	connectBase = ConnectBase.ConnectFactory(reader);
    	datas       = reader.ReadToEnd();
    	
    }
    
    public void OnReader(IoSession session,Object param)
    {
    	TransServerManager manager       = (TransServerManager)param;
    	int type                         = connectBase.type;
    	if(type == TServerConfig.TYPE_CLIENT)
    	{
    		long id                      = GetId();
        	ConnectClient dstClient      = manager.GetClient(id);
        	if(dstClient != null && dstClient.fromRoute != null)
        	{
        		long fromServerId = dstClient.fromRoute.id;
        		if(fromServerId == TServerConfig.SERVER_ID)
        		{
            		TServerToClientTransWriter cQueryResponse = new TServerToClientTransWriter(datas);
            		dstClient.session.write(cQueryResponse);
        		}
        		else//post next transServer
        		{
        			ConnectTServer tServer = manager.GetTServer(fromServerId);
        			TServerToTServerTransWriter tServerWriter = new TServerToTServerTransWriter(dstClient,datas);
        			tServer.session.write(tServerWriter);
        			
        		}
        		
        	}
        	
    	}
    	else if(type == TServerConfig.TYPE_LSERVER)
    	{
    		long id                    = GetId();
    		ConnectLServer dstServer   = manager.GetLServer(id);
    		if(dstServer != null &&  dstServer.fromRoute != null)
    		{
    			long fromServerId = dstServer.fromRoute.id;
    			if(fromServerId == TServerConfig.SERVER_ID)
    			{
    				TServerToLogicTransWriter cQueryResponse = new TServerToLogicTransWriter(dstServer,datas);
        			dstServer.session.write(cQueryResponse);	
    			}
    			else//post next transServer
    			{
    				ConnectTServer tServer = manager.GetTServer(fromServerId);
        			TServerToTServerTransWriter tServerWriter = new TServerToTServerTransWriter(dstServer,datas);
        			tServer.session.write(tServerWriter);
    			}
    							
    		}	
    	}
    }
}
