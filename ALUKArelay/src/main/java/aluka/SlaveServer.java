package aluka;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class SlaveServer implements Runnable {

	private ServerSocket server;

	private final static byte MAX_SIZE = 6;
	
	private static short cmp = 0; 

	private ConcurrentHashMap<Short, ClientManager> connections = new ConcurrentHashMap<>();

	public SlaveServer(short port) {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("[ - ] - Cannot start slave server on port " + port);
			System.exit(0);
		}
	}

	public boolean isFull() {
		return connections.size() == MAX_SIZE;
	}

	@Override
	public void run() {
		while (!server.isClosed()) {
			Socket socket = null;
			try {
				if (!this.isFull()) {
					socket = server.accept();
					System.out.println("Connection on port " + server.getLocalPort() + " at address " + socket.getInetAddress().toString());
					if(cmp == 65536 ) cmp = 0;
					short id = cmp++;
					ClientManager connectionObject = new ClientManager(socket, this,id);
					Thread thread = new Thread(connectionObject);
					thread.start();
					connections.put(id, connectionObject);
				}
			} catch (IOException e) {
				System.err.println("[ - ] - Client disconnected ");
			}

		}

	}

	public synchronized void disconnect(Short id) {
		connections.remove(id);
	}
	
	public int getPort() {
		return this.server.getLocalPort();
	}
}
