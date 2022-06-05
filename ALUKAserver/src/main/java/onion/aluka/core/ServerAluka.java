package onion.aluka.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;

import onion.aluka.AlukAserverApplication;
import onion.aluka.ClientManager;
import onion.aluka.LoggerManagement;

public class ServerAluka implements Runnable{

	//Variables
	private static ServerAluka instance;
	private static int PORT = 45000;
	private static ServerSocket serverSocket;
	private final static LoggerManagement loggerManagement = LoggerManagement.getInstance();

	
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	private ServerAluka() {
		try {
			loggerManagement.log(Level.INFO, "Initialising server on port " + PORT);
			serverSocket = new ServerSocket(PORT);
		} catch (Exception e) {
			loggerManagement.log(Level.SEVERE, "Can not initialize Server");
		}
	}

	public static ServerAluka getInstance() {
		if(instance == null)
			instance = new ServerAluka();
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
				Thread clients = new Thread(new ClientManager(socket));
				clients.start();
			}catch (IOException e) {
				loggerManagement.log(Level.WARNING, "victim disconnected");
			}
			
		}
	}
}
