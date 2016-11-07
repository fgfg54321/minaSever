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
package org.apache.mina.login;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.constants.Constants;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.utils.PropertiesUtils;

public class LoginTcpServer extends IoHandlerAdapter
{
	public static int    listenPort;
    public static String ipAddress;
    
    public void Initialize()
    {
    	try
		{
			Map<String, String> map = PropertiesUtils
					.LoadProperties(Constants.GATE_CONFIG);
			
			ipAddress  = map.get("ip");
			listenPort = Integer.parseInt(map.get("port"));

			NioSocketAcceptor acceptor = new NioSocketAcceptor();
			acceptor.setHandler(this);
			acceptor.bind(new InetSocketAddress(listenPort));

			System.out.println("server started" + listenPort);
			
		} 
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
    }
    
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        session.closeNow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
    
        // If we want to test the write operation, uncomment this line
        session.write(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionClosed(IoSession session) throws Exception {
    	super.sessionClosed(session);
        System.out.println("Session closed...");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionCreated(IoSession session) throws Exception {
    	long id = session.getId();
        System.out.println("Session created...");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("Session idle...");
    }

    /**
     * {@inheritDoc}
     * @param session the current seession
     * @throws Exception If something went wrong
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("Session Opened...");
    }

    /**
     * Create the TCP server
     * 
     * @throws IOException If something went wrong
     */
    public LoginTcpServer() throws IOException
    {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
        acceptor.setHandler(this);
        
        acceptor.bind(new InetSocketAddress(listenPort));

        System.out.println("Server started" + listenPort);
    }

    /**
     * The entry point.
     * 
     * @param args The arguments
     * @throws IOException If something went wrong
     */
    public static void main(String[] args) throws IOException {
        new LoginTcpServer();
    }
}
