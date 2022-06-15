package aluka;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@FunctionalInterface
interface Operation<T,R> {
	
	public void doOperation(T t,R r);
}

class Waiter implements Runnable{

	private ServerRelay server;
	
	public Waiter(ServerRelay server) {
		this.server = server;
	}
	
	@Override
	public void run() {
		try {
			Socket socket = this.server.getServerSocket().accept();
			
			Operation<List<Socket>,Socket> operation = new Operation<List<Socket>,Socket>(){
				
				public void doOperation(List<Socket> socketsWating,Socket received) {
					socketsWating.add(received);
				}
				
			};
			
			this.server.doOperation(operation, socket);
			
		} catch (IOException e) {
			System.out.println("[ - ] - Server problem at accepting");
			System.exit(1);
		}
		
	}
	
}

public class ServerRelay implements Runnable {

	private static ServerRelay serverRelay;
	private final static byte MAX_SERVERS = 10;
	private SlaveServer[] slaves = new SlaveServer[MAX_SERVERS];
	private short ports[] = new short[] { 8081, 8082, 8083, 8084, 8085, 8086, 8087, 8088, 8089, 8090 };
	private List<Socket> socketsWating = new ArrayList<Socket>();
	
	
	// Public element

	private final static short PRINCIPAL_PORT = 8080;
	private ServerSocket principalServer;
	private boolean isRunning = true;

	private ServerRelay() {
		try {
			System.out.println("[ + ] - Starting Principal Server");
			principalServer = new ServerSocket(PRINCIPAL_PORT);

			// Slaves

			for (int i = 0; i < MAX_SERVERS; i++) {
				System.out.println(" ------ Starting Slave number " + (i + 1) + " : " + ports[i]);
				slaves[i] = new SlaveServer(ports[i]);
				Thread thread = new Thread(slaves[i]);
				thread.start();
			}
			System.out.println("Server started on port " + principalServer.getLocalPort());
		} catch (IOException e) {
			System.out.println("[ - ] - Can not start the server");
			System.exit(1);
		}
	}
	
	public synchronized void doOperation(Operation<List<Socket>,Socket> operation,Socket socket) {
		operation.doOperation(socketsWating,socket);
	}

	public static ServerRelay getInstance() {
		if (serverRelay == null) {
			serverRelay = new ServerRelay();
			return serverRelay;
		} else {
			return serverRelay;
		}
	}
	
	public ServerSocket getServerSocket() {
		return this.principalServer;
	}

	@Override
	public void run() {
		while (isRunning) {
			try {
				Socket socket = principalServer.accept();
				
				System.out.println("Connection on port " + principalServer.getLocalPort() + " at adress " + socket.getInetAddress().toString());
				// Slave flux manager
				Integer portEmpty = null;
				for (SlaveServer slave : this.slaves) {
					if (!slave.isFull()) {
						portEmpty = slave.getPort();
						break;
					}
				}

				if (portEmpty == null) {
					socketsWating.add(socket);
				} else {
					if (!socketsWating.isEmpty()) {
						
						//Because i m using doOperation with synchronized stereotype to manage thread safety
						Operation<List<Socket>,Socket> operation = new Operation<List<Socket>,Socket>() {
							
							public void doOperation(List<Socket> socketsWating,Socket received) {
								socketsWating.add(received);
								received = socketsWating.get(0);
								socketsWating.remove(0);
							}
							
						};
						this.doOperation(operation, socket);
					}
				}
				
				try(DataOutputStream dos = new DataOutputStream(socket.getOutputStream())){
					dos.writeShort(portEmpty);
				}catch (IOException e) {
					System.err.println("[ - ] - Client disconnected");
				}

			} catch (IOException e) {
				System.out.println("[ - ] - Server problem at accepting");
				System.exit(0);
				;
			}
		}

	}
}
