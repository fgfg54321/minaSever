package org.apache.mina.tcp.base.transserver.protocol.transmission;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.log.MLog;
import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.tcp.base.stream.SplitPackage;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.struct.ConnectClient;
import org.apache.mina.tcp.base.struct.ConnectLServer;
import org.apache.mina.tcp.base.struct.ConnectTServer;
import org.apache.mina.tcp.base.transserver.TServerConfig;
import org.apache.mina.tcp.base.transserver.TransServerManager;
import org.apache.mina.utils.SVZipUtils;

public class TServerToClientTransReader extends TCPBaseReader
{
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
    
    @Override
    public  void ReadContent(ProtocolStreamReader reader)
    {
    	reader.Reset();
    	datas = reader.ReadToEnd();
    }
    
    @Override
    public boolean Read(ProtocolStreamReader reader,IoSession ioSession,ProtocolDecoderOutput out)
    {
    	ReadHeader(reader);
    	
        reader.ReadInt32();//sliceIndex;
        
        ReadContent(reader);
      
        out.write(this);
        
        return true;
    }
    
    @Override
    public void OnReader(IoSession session,Object param)
    {
    	TransServerManager manager        = (TransServerManager)param;
		ConnectLServer server          =  manager.GetLServer(dstServerId);
		if(server != null)
		{
			ConnectClient cClient                       = manager.GetClient(session);
			TServerToLogicTransWriter tsToLogicWriter   = new TServerToLogicTransWriter(cClient,datas);
			tsToLogicWriter.setDstServerId(dstServerId);
    		server.session.write(tsToLogicWriter);
		}
		else 
		{   
			//post from transmission server if exist
			ConnectTServer tServer = manager.GetTransServerConnector();
			if(tServer == null)
			{
				ConnectClient cClient                        = manager.GetClient(session);
				TServerToTServerTransWriter serverTransWriter = new TServerToTServerTransWriter(cClient,datas);
				tServer.session.write(serverTransWriter);
			}
		}
    }
}
