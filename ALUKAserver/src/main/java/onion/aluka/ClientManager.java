package onion.aluka;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

public class ClientManager implements Runnable {

	private Socket socket;
	private PublicKey publicKey;
	private PrivateKey privateKey;

	public ClientManager(Socket socket,PublicKey publicKey,PrivateKey privateKey) {
		System.out.println("Received");
		this.socket = socket;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	@Override
	public void run() {
		try(BufferedInputStream bis = new BufferedInputStream(this.socket.getInputStream())) {
			System.out.println("[ + ] - Reading key from this client");
			int l;
			byte[] bytes = new byte[1024];
			String keyBase64 = "";
			while((l=bis.read(bytes)) > 0) {
				keyBase64 += new String(bytes,0,l);
			}
			System.out.println(keyBase64);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
