package aluka.configuration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class NormalServerConnexion implements Configuration{

	@Override
	public Socket getServerConnexion() throws IOException{
		Socket socket = new Socket(InetAddress.getByName("127.0.0.1"),45000);
		return socket;
	}

}
