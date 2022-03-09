package onion.aluka;

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

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AlukAserverApplication implements CommandLineRunner {
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public static void main(String[] args) {
		SpringApplication.run(AlukAserverApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			File privateKeyFile = new File("privateKey.obj");
			File publicKeyFile = new File("publicKey.obj");

			if (privateKeyFile.exists() && publicKeyFile.exists()) {
				System.out.println(privateKeyFile.getAbsolutePath());
				ObjectInputStream privateReader = null;
				ObjectInputStream publicReader = null;
				try {
					privateReader = new ObjectInputStream(new FileInputStream(privateKeyFile));
					publicReader = new ObjectInputStream(new FileInputStream(publicKeyFile));
					this.publicKey = (PublicKey) publicReader.readObject();
					this.privateKey = (PrivateKey) privateReader.readObject();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} finally {
					try {
						privateReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						publicReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} else {
				try {
					KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
					generator.initialize(1024);
					KeyPair keyPair = generator.generateKeyPair();
					publicKey = keyPair.getPublic();
					privateKey = keyPair.getPrivate();

					privateKeyFile.deleteOnExit();
					publicKeyFile.deleteOnExit();

					try {
						privateKeyFile.createNewFile();
						publicKeyFile.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}

					ObjectOutputStream privateWriter = null;
					ObjectOutputStream publicWriter = null;
					try {
						privateWriter = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
						publicWriter = new ObjectOutputStream(new FileOutputStream(publicKeyFile));

						privateWriter.writeObject(privateKey);
						publicWriter.writeObject(publicKey);
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							privateWriter.close();
							publicWriter.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				} catch (NoSuchAlgorithmException e) {
					System.err.println("Algorithm not found");
				}

			}
			System.out.println("Server");
			ServerSocket server = new ServerSocket(45000);
			while (true) {
				Socket socket = server.accept();
				Thread clients = new Thread(new ClientManager(socket, publicKey, privateKey));
				clients.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
