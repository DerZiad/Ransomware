package aluka.core;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

import aluka.configuration.Configuration;

public class ClientServerManager {

	private LoggerManagement logger = LoggerManagement.getInstance();

	public void exportKey(String key) throws IOException {
		Socket serverConnexion = Configuration.getSimpleConnexion();
		logger.log(Level.INFO, "Exporting Sync Key");
		BufferedOutputStream bos = new BufferedOutputStream(serverConnexion.getOutputStream());
		bos.write(key.getBytes());
		bos.flush();
	}
	
	
}
