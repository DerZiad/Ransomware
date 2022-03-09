package aluka.core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerManager {
	
	
	public static void main(String[] args) throws IOException {
		Socket socket = new Socket(InetAddress.getByName("105.158.73.165"),45000);
	}
}
