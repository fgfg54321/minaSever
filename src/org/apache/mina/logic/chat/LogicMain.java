package org.apache.mina.logic;

import java.net.InetSocketAddress;
import java.util.Map;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.tcp.base.constants.Constants;
import org.apache.mina.tcp.base.transserver.TransTcpServerListener;
import org.apache.mina.tcp.base.transserver.codec.TServerCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.apache.mina.utils.PropertiesUtils;

public class LogicMain
{
	public static int    listenPort;
    public static String ipAddress;
    
    public TransTcpServerListener loginTcpServer = new TransTcpServerListener();
    
	private IoConnector	     connector;
	private static IoSession session;
	
    public LogicTcpClient loginTcpClient = new LogicTcpClient();
    	
	public void ServerStart()
    {
    	try
		{
			Map<String, String> map = PropertiesUtils.LoadProperties(Constants.GATE_CONFIG);
			
			ipAddress  = map.get("ip");
			listenPort = Integer.parseInt(map.get("port"));

			NioSocketAcceptor acceptor = new NioSocketAcceptor();
			
	        acceptor.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new TServerCodecFactory()));
	        
			acceptor.setHandler(loginTcpServer);
			acceptor.bind(new InetSocketAddress(listenPort));

			System.out.println("server started" + listenPort);
			
		} 
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
    }
	
	public void ClientConnect()
	{
		connector = new NioSocketConnector();
		connector.setHandler(loginTcpClient);
		ConnectFuture connFuture = connector.connect(new InetSocketAddress(ipAddress, listenPort));
		connFuture.awaitUninterruptibly();

		session = connFuture.getSession();
	}
}
