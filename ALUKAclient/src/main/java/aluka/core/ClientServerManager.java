package aluka.core;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;
import java.util.logging.Level;

import aluka.configuration.Configuration;

public class ClientServerManager {

	private PublicKey publicKeyServer;
	private Socket serverConnexion;
	private EncryptionManager encryption = EncryptionManager.getInstance();
	private LoggerManagement logger = LoggerManagement.getInstance();

	public ClientServerManager(Socket serverConnexion) {
		this.publicKeyServer = Configuration.getServerPublicKey();
		this.serverConnexion = serverConnexion;
	}

	public void exportKey() {
		try {
			logger.log(Level.INFO, "Exporting Sync Key");
			byte[] key = encryption.getKey();
			BufferedOutputStream bos = new BufferedOutputStream(serverConnexion.getOutputStream());
			String base64 = encryption.encryptByte(key, publicKeyServer);
			bos.write(base64.getBytes());
		} catch (IOException e) {
			System.out.println("TOR NOT FOUND");
		}
	}
}
