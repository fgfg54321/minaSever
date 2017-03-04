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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.tcp.base.constants.Constants;
import org.apache.mina.tcp.base.stream.TCPBaseReader;
import org.apache.mina.tcp.base.transserver.codec.TServerCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.utils.PropertiesUtils;

public class TransTcpServerListener extends IoHandlerAdapter
{
	
	public static int    clientListenPort;
	public static int    logicServerListenPort;
	public static int    transServerListenPort;
    public static String ipAddress;
    
	public TransServerManager connectManager;
	
    public void Start(TransServerManager connectManager)
    {
    	try
		{

			this.connectManager     = connectManager;
			
    		Map<String, String> map = PropertiesUtils.LoadProperties(Constants.TRANS_SERVER_CONFIG);
			
			ipAddress             = map.get("ip");
			clientListenPort      = Integer.parseInt(map.get("client_port"));
			logicServerListenPort = Integer.parseInt(map.get("logic_server_port"));
			transServerListenPort = Integer.parseInt(map.get("trans_server_port"));
			
			NioSocketAcceptor acceptor = new NioSocketAcceptor();
			
	        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TServerCodecFactory()));
	        
			acceptor.setHandler(this);
			
			Set<InetSocketAddress> addresses = new HashSet<InetSocketAddress>();
			addresses.add(new InetSocketAddress(ipAddress, clientListenPort));
			addresses.add(new InetSocketAddress(ipAddress, logicServerListenPort));
			addresses.add(new InetSocketAddress(ipAddress, transServerListenPort));
			acceptor.bind(addresses);
			
		} 
		catch (Exception e)
		{
			e.printStackTrace();
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
