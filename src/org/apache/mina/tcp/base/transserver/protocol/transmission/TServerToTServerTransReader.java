package org.apache.mina.tcp.base.transserver.protocol.transmission;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.ConnectBase;
import org.apache.mina.tcp.base.struct.ConnectClient;
import org.apache.mina.tcp.base.struct.ConnectLServer;
import org.apache.mina.tcp.base.struct.ConnectTServer;
import org.apache.mina.tcp.base.transserver.TServerConfig;
import org.apache.mina.tcp.base.transserver.TransServerManager;

public class TServerToTServerTransReader extends TCPBaseReader
{

	public ConnectBase  connectBase;
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
    
    @Override
    public  void ReadContent(ProtocolStreamReader reader)
    {

    	connectBase = ConnectBase.ConnectFactory(reader);
    	datas       = reader.ReadToEnd();
    	
    }
    
    @Override
    public boolean Read(ProtocolStreamReader reader,IoSession ioSession,ProtocolDecoderOutput out)
    {
    	ReadHeader(reader);
    	
        int  sliceIndex          = reader.ReadInt32();
        
        ReadContent(reader);
        out.write(this);
      
        return true;
    }
    
    @Override
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
            		TServerToClientTransWriter clientTransWriter = new TServerToClientTransWriter(datas);
            		dstClient.session.write(clientTransWriter);
        		}
        		else//post from transServer
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
    				TServerToLogicTransWriter logicTransWriter = new TServerToLogicTransWriter(dstServer,datas);
    				logicTransWriter.setDstServerId(id);
        			dstServer.session.write(logicTransWriter);	
    			}
    			else//post from transServer
    			{
    				ConnectTServer tServer = manager.GetTServer(fromServerId);
        			TServerToTServerTransWriter tServerWriter = new TServerToTServerTransWriter(dstServer,datas);
        			tServer.session.write(tServerWriter);
    			}
    							
    		}	
    	}
    }
}
