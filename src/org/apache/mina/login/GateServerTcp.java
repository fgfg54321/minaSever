package org.apache.mina.login;

import java.net.InetSocketAddress;
import java.util.Map;

import org.apache.mina.constants.Constants;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.utils.PropertiesUtils;

public class GateServerTcp extends IoHandlerAdapter
{
	public int	listenPort;

	public void initialize()
	{
		try
		{
			Map<String, String> map = PropertiesUtils
					.LoadProperties(Constants.GATE_CONFIG);
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
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception
	{
		// TODO Auto-generated method stub
		super.exceptionCaught(session, cause);
	}

	@Override
	public void inputClosed(IoSession session) throws Exception
	{
		// TODO Auto-generated method stub
		super.inputClosed(session);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception
	{
		// TODO Auto-generated method stub
		super.messageReceived(session, message);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception
	{
		// TODO Auto-generated method stub
		super.messageSent(session, message);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception
	{
		// TODO Auto-generated method stub
		super.sessionClosed(session);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception
	{
		// TODO Auto-generated method stub
		super.sessionCreated(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception
	{
		// TODO Auto-generated method stub
		super.sessionIdle(session, status);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception
	{
		// TODO Auto-generated method stub
		super.sessionOpened(session);
	}

}
