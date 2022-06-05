package onion.aluka.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.logging.Level;

import javax.crypto.Cipher;

import onion.aluka.LoggerManagement;

public class EntityManager {

	// Constantes
	private final static String ALGORITHM = "RSA";
	private final static short BYTE_NUMBER = 1024;

	// Variables
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private static EntityManager instance;
	private static File privateKeyFile;
	private static File publicKeyFile;
	private static LoggerManagement loggerManagement = LoggerManagement.getInstance();

	private EntityManager() {
		loggerManagement.log(Level.INFO, "Initialising EntityManager");
		privateKeyFile = new File("privateKey.obj");
		publicKeyFile = new File("publicKey.obj");

		if (privateKeyFile.exists() && publicKeyFile.exists()) {
			System.out.println(privateKeyFile.getAbsolutePath());
			loadKeys();

		} else {
			
			deleteOnExist(privateKeyFile);
			deleteOnExist(publicKeyFile);

			try {
				privateKeyFile.createNewFile();
				publicKeyFile.createNewFile();
			} catch (IOException e) {
				loggerManagement.log(Level.SEVERE, "Can't create files",e);
			}
			generateKeyPair();
			saveKeys();
		}
	}

	// Private functions
	private void deleteOnExist(File file) {
		if (file.exists())
			file.delete();
	}

	private void generateKeyPair() {
		try {
			loggerManagement.log(Level.INFO, "Generating key pairs");
			KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
			generator.initialize(BYTE_NUMBER);
			KeyPair keyPair = generator.generateKeyPair();
			publicKey = keyPair.getPublic();
			privateKey = keyPair.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			loggerManagement.log(Level.SEVERE, "Algorithm not found",e);
			System.exit(1);
		}
	}

	private void saveKeys() {
		loggerManagement.log(Level.INFO, "Saving keys");
		deleteOnExist(privateKeyFile);
		deleteOnExist(publicKeyFile);
		
		try (ObjectOutputStream privateWriter = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
				ObjectOutputStream publicWriter = new ObjectOutputStream(new FileOutputStream(publicKeyFile));) {

			privateWriter.writeObject(privateKey);
			publicWriter.writeObject(publicKey);
		} catch (IOException e) {
			loggerManagement.log(Level.SEVERE, "Error writing in files");
		}
	}

	private void loadKeys() {
		loggerManagement.log(Level.INFO,"Loading keys");
		try (ObjectInputStream privateReader = new ObjectInputStream(new FileInputStream(privateKeyFile));
				ObjectInputStream publicReader = new ObjectInputStream(new FileInputStream(publicKeyFile))) {
			this.publicKey = (PublicKey) publicReader.readObject();
			this.privateKey = (PrivateKey) privateReader.readObject();
		} catch (Exception e) {
			loggerManagement.log(Level.WARNING,"Content is invalid");
			deleteOnExist(privateKeyFile);
			deleteOnExist(publicKeyFile);
			generateKeyPair();
			saveKeys();
		}
	}
	
	private String encodeToBase64(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}

	private byte[] decodeToBase64(String data) {
		return Base64.getDecoder().decode(data);
	}
	
	public String encryptByte(byte[] messageBytes) {
		Cipher cipher;
		String encryptedMsg = "";
		try {
			cipher = Cipher.getInstance(ALGORITHM +"/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
			byte[] encryptedMsgBytes = cipher.doFinal(messageBytes);
			encryptedMsg = encodeToBase64(encryptedMsgBytes);
		} catch (GeneralSecurityException e) {
			loggerManagement.log(Level.WARNING, "Encryption configuration problem", e);
		}
		return encryptedMsg;
	}
	
	public String decryptByte(String msg) {
		String msgDecrypted = "";
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(ALGORITHM + "/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
			byte[] messageBytes = decodeToBase64(msg);
			byte[] decryptedMsg = cipher.doFinal(messageBytes);
			msgDecrypted = new String(decryptedMsg);
		} catch (GeneralSecurityException e) {
			loggerManagement.log(Level.WARNING, "Decryption configuration problem", e);
		}
		return msgDecrypted;

	}

	public static EntityManager getInstance() {
		if (instance == null)
			instance = new EntityManager();
		return instance;
	}
}
