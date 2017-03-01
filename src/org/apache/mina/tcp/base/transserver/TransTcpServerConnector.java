/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.mina.tcp.base.transserver;

import java.net.InetSocketAddress;
import java.util.Map;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.example.imagine.step1.codec.ImageCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.tcp.base.constants.Constants;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.transserver.codec.TServerCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.apache.mina.utils.PropertiesUtils;

public class TransTcpServerConnector extends IoHandlerAdapter
{
	public static int    connectTimtOut;
	public static int    port;
    public static String ipAddress;
    
	public TransServerManager connectManager;
	
    public void Start(TransServerManager connectManager)
    {
    	try
		{
    		this.connectManager = connectManager;
			Map<String, String> map = PropertiesUtils.LoadProperties(Constants.TRANS_SERVER_CONFIG);
			
			String getRouteUrl = map.get("route_url");
			
			int serverId = 0;
			String serverName = "";
			ipAddress          = map.get("ip");
			port               = Integer.parseInt(map.get("clientPort"));

			
		
			InetSocketAddress innerSocket = new InetSocketAddress(ipAddress, port);
					
			NioSocketConnector connector = new NioSocketConnector();
	        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TServerCodecFactory()));
	        connector.setHandler(this);
	        ConnectFuture connectFuture = connector.connect(innerSocket);
	        connectFuture.awaitUninterruptibly(connectTimtOut);
	        IoSession session = connectFuture.getSession();
	        
	        connectManager.SetTransServerConnector(serverId,serverName,session);
	        
		} 
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
    }
    
	public void sessionCreated(IoSession session) throws Exception
    {
		connectManager.SetConnectBelong(session);

    }
	
	@Override
    public void sessionOpened(IoSession session) throws Exception
    {
		
    }
	
	public void sessionClosed(IoSession session) throws Exception
	{
		connectManager.LoginOut(session);
	}

    @Override
    public void messageReceived(final IoSession session, Object message) throws Exception 
    {
    	final TCPBaseReader serverRequest = (TCPBaseReader)message;
		serverRequest.OnReader(session, connectManager);
    }
}
