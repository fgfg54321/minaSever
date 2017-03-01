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
package org.apache.mina.tcp.base.logicserver;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.tcp.base.constants.Constants;
import org.apache.mina.tcp.base.logicserver.codec.LogicCodecFactory;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.apache.mina.utils.PropertiesUtils;

public class LogicTcpServer extends IoHandlerAdapter
{
	
	public static int    transServerListenPort;
    public static String transServerIpAddress;
 
    public IoSession session;
    
    public LogicServerManager connectManager = new LogicServerManager();
    
    public void Start(LogicServerManager connectManager)
    {
    	try
		{
    		this.connectManager     = connectManager;
    		
			Map<String, String> map = PropertiesUtils.LoadProperties(Constants.LOGIC_SERVER_CONFIG);
			
			transServerIpAddress    = map.get("trans_server_ip");
			transServerListenPort   = Integer.parseInt(map.get("trans_server_port"));

    		NioSocketConnector connector = new NioSocketConnector();
    		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new LogicCodecFactory()));
	        connector.setHandler(this);
	        
	        List<InetSocketAddress> addresses = new ArrayList<InetSocketAddress>();
			addresses.add(new InetSocketAddress(transServerIpAddress, transServerListenPort));
			
	        ConnectFuture connFuture          = connector.connect(addresses.get(0));

	        connFuture.awaitUninterruptibly();

	        int transServerId = 0;
	        session = connFuture.getSession();
	        connectManager.ConnectTServer(session,transServerId);
    	        
		} 
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
    }
    
	public void sessionCreated(IoSession session) throws Exception
    {
		
    }
	
	@Override
    public void sessionOpened(IoSession session) throws Exception
    {
		
    }
	
	public void sessionClosed(IoSession session) throws Exception
	{
		connectManager.TServerTick(session);
	}

    @Override
    public void messageReceived(final IoSession session, Object message) throws Exception 
    {
    	final TCPBaseReader serverRequest = (TCPBaseReader)message;
		serverRequest.OnReader(session, connectManager);
    }
}
