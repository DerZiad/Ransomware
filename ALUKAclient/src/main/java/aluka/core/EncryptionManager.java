package aluka.core;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import aluka.configuration.Configuration;

public class EncryptionManager {

	// Variables
	private static final String ALGORITHM = "AES";
	private static final short byteNumber = 256;

	private static EncryptionManager instance;
	private LoggerManagement logger = LoggerManagement.getInstance();
	private SecretKey key;

	private EncryptionManager() {
		try {
			key = createAESKey();
		} catch (NoSuchAlgorithmException e) {
			logger.log(Level.SEVERE, "Algorithm not found", e);
		}
	}

	// Inside Functions
	private String encodeToBase64(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}

	private byte[] decodeToBase64(String data) {
		return Base64.getDecoder().decode(data);
	}

	public static EncryptionManager getInstance() {
		if (instance == null)
			instance = new EncryptionManager();
		return instance;
	}

	private SecretKey createAESKey() throws NoSuchAlgorithmException {
		logger.log(Level.INFO, "Generating " + ALGORITHM + " " + byteNumber + " KEY");
		SecureRandom securerandom = new SecureRandom();
		KeyGenerator keygenerator = KeyGenerator.getInstance(ALGORITHM);
		keygenerator.init(256, securerandom);
		SecretKey key = keygenerator.generateKey();
		return key;
	}

	public String encryptByte(byte[] messageBytes) {
		Cipher cipher;
		String encryptedMsg = "";
		try {
			cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, this.key);
			byte[] encryptedMsgBytes = cipher.doFinal(messageBytes);
			encryptedMsg = encodeToBase64(encryptedMsgBytes);
		} catch (GeneralSecurityException e) {
			logger.log(Level.WARNING, "Encryption configuration problem", e);
		}
		return encryptedMsg;
	}

	public String encryptByte(byte[] messageBytes, PublicKey publicKey) {
		logger.log(Level.INFO, "Encrypting with server Public Key");
		Cipher cipher;
		String encryptString = "";
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] encryptedMsg = cipher.doFinal(messageBytes);
			encryptString = encodeToBase64(encryptedMsg);
		} catch (GeneralSecurityException e) {
			logger.log(Level.SEVERE, "Can not encrypt with server public key");
		}
		return encryptString;
	}

	public String decryptByte(String msg) {
		String msgDecrypted = "";
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, this.key);
			byte[] messageBytes = decodeToBase64(msg);
			byte[] decryptedMsg = cipher.doFinal(messageBytes);
			msgDecrypted = new String(decryptedMsg);
		} catch (GeneralSecurityException e) {
			logger.log(Level.WARNING, "Decryption configuration problem", e);
		}
		return msgDecrypted;

	}

	public String destroyKey() {
		PublicKey serverPublicKey = Configuration.getServerPublicKey();
		String base64 = this.encryptByte(key.getEncoded(), serverPublicKey);
		key = null;
		return base64;
	}

}
