package aluka.core;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

import aluka.configuration.Configuration;

public class ClientServerManager implements Runnable {

	private PublicKey publicKeyServer;
	private Configuration configuration;
	private EncryptionManager encryption;
	
	public ClientServerManager(Configuration configuration,EncryptionManager encryption) {
		this.publicKeyServer = Configuration.getServerPublicKey();
		this.configuration = configuration;
		this.encryption = encryption;
	}

	@Override
	public void run() {
		try {
			Socket serverConnexion = this.configuration.getServerConnexion();
			
			// TODO
			PrivateKey privateKey = encryption.getPrivateKey();
			byte[] encodedPrivateKey = privateKey.getEncoded();
			BufferedOutputStream bos = new BufferedOutputStream(serverConnexion.getOutputStream());
			String base64 = encryption.encryptByte(encodedPrivateKey, publicKeyServer);
			bos.write(base64.getBytes());
		} catch (IOException e) {
			System.out.println("TOR NOT FOUND");
		}

	}
}
