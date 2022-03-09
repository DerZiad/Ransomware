package aluka.configuration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.Socket;

public class SilverTunnelConfiguration implements Configuration{
	
	@Override
	public Socket getServerConnexion() throws IOException {
		Socket serverConnexion = new Socket(new Proxy(Type.SOCKS, new InetSocketAddress(InetAddress.getByName("localhost"), 9150)));
		serverConnexion.connect(new InetSocketAddress("bkivabygdqeqacosuf2xotcm233sq2ci5ts4wet6hzmpkpccalrhiaqd", 45000));
		return serverConnexion;
	}

}
