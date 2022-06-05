package aluka.core;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

public class ClientServerManager {

	private Socket serverConnexion;
	private LoggerManagement logger = LoggerManagement.getInstance();

	public ClientServerManager(Socket serverConnexion) {
		this.serverConnexion = serverConnexion;
	}

	public void exportKey(String key) throws IOException {
		logger.log(Level.INFO, "Exporting Sync Key");
		BufferedOutputStream bos = new BufferedOutputStream(serverConnexion.getOutputStream());
		bos.write(key.getBytes());
		bos.flush();
	}
	
	
}
