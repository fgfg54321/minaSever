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

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class LoginTcpClient extends IoHandlerAdapter
{
	/** The connector */
	private IoConnector			connector;

	/** The session */
	private static IoSession	session;

	/** The buffer containing the message to send */
	private IoBuffer			buffer	= IoBuffer.allocate(8);

	/** Timers **/
	private long				t0;
	private long				t1;

	/** the counter used for the sent messages */
	private CountDownLatch		counter;

	/**
	 * Create the UdpClient's instance
	 */
	public LoginTcpClient()
	{
		connector = new NioSocketConnector();
		connector.setHandler(this);
		ConnectFuture connFuture = connector.connect(new InetSocketAddress(
				LoginTcpServer.ipAddress, LoginTcpServer.listenPort));
		connFuture.awaitUninterruptibly();

		session = connFuture.getSession();
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception
	{
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception
	{
		long received = ((IoBuffer) message).getLong();

		if (received != counter.getCount())
		{
			System.out.println("Error !");
			session.closeNow();
		} else
		{
			if (counter.getCount() == 0L)
			{
				t1 = System.currentTimeMillis();

				System.out.println("------------->  end " + (t1 - t0));
				session.closeNow();
			} else
			{
				counter.countDown();

				buffer.flip();
				buffer.putLong(counter.getCount());
				buffer.flip();
				session.write(buffer);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void messageSent(IoSession session, Object message) throws Exception
	{
		if (counter.getCount() % 10000 == 0)
		{
			System.out.println("Sent " + counter + " messages");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception
	{
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception
	{
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception
	{
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sessionOpened(IoSession session) throws Exception
	{
	}

	/**
	 * The main method : instanciates a client, and send N messages. We sleep
	 * between each K messages sent, to avoid the server saturation.
	 * 
	 * @param args
	 *            The arguments
	 * @throws Exception
	 *             If something went wrong
	 */
	public static void main(String[] args) throws Exception
	{
		LoginTcpClient client = new LoginTcpClient();

		client.t0 = System.currentTimeMillis();
		client.counter = new CountDownLatch(LoginTcpServer.MAX_RECEIVED);
		client.buffer.putLong(client.counter.getCount());
		client.buffer.flip();
		session.write(client.buffer);
		int nbSeconds = 0;

		while ((client.counter.getCount() > 0) && (nbSeconds < 120))
		{
			// Wait for one second
			client.counter.await(1, TimeUnit.SECONDS);
			nbSeconds++;
		}

		client.connector.dispose(true);
	}
}
