package aluka.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.Socket;
import java.security.PublicKey;

public interface Configuration {

	public Socket getServerConnexion() throws IOException;

	public static PublicKey getServerPublicKey() {
		File file = new File("publicKey.obj");
		ObjectInputStream keyReader = null;
		PublicKey publicKey = null;
		try {
			keyReader = new ObjectInputStream(new FileInputStream(file));
			publicKey = (PublicKey) keyReader.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				keyReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return publicKey;

	}

}
