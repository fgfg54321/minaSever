package org.apache.mina.tcp.base.IoHandler;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.log.MLog;

public class BaseServerTcp extends IoHandlerAdapter
{
	
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception
    {
    	MLog.Error("%ld error %s\n%s",session.getId(),cause.toString(), cause.getStackTrace().toString());
        cause.printStackTrace();
        session.closeNow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception 
    {
    	super.messageReceived(session, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionClosed(IoSession session) throws Exception
    {
    	super.sessionClosed(session);
    	MLog.Debug("Session closed...%d",session.getId());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionCreated(IoSession session) throws Exception
    {
    	long id = session.getId();
    	MLog.Debug("Session sessionCreated...%d",session.getId());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception 
    {
        System.out.println("Session idle...");
    }

    /**
     * {@inheritDoc}
     * @param session the current seession
     * @throws Exception If something went wrong
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception
    {
    	InetSocketAddress socketAdress = (InetSocketAddress)session.getLocalAddress();
    	int port                       = socketAdress.getPort();
    	
    	
    	
        System.out.println("Session Opened...");
    }

}
