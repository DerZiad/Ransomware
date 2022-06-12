package aluka.core;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import aluka.configuration.Configuration;
import aluka.exception.InvalidKeyException;

public class EncryptionManager {

	// Variables
	private static final String SYNC_ENCRYPTION_ALGORITHM = "AES";
	private static final String HASH_ALGORITHM = "SHA256";
	private static final short byteNumber = 256;

	private static EncryptionManager instance;
	private static StateManager stateManager = StateManager.getInstance();
	private static LoggerManagement logger = LoggerManagement.getInstance();
	private SecretKey key;
	private String signature;

	private EncryptionManager() {
		if (!stateManager.isSigned()) {
			try {

				key = createAESKey();
				signIt();
			} catch (NoSuchAlgorithmException e) {
				logger.log(Level.SEVERE, "Algorithm not found", e);
				System.exit(1);
			}
		} else {
				signature = stateManager.getSignature();
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
		logger.log(Level.INFO, "Generating " + SYNC_ENCRYPTION_ALGORITHM + " " + byteNumber + " KEY");
		SecureRandom securerandom = new SecureRandom();
		KeyGenerator keygenerator = KeyGenerator.getInstance(SYNC_ENCRYPTION_ALGORITHM);
		keygenerator.init(256, securerandom);
		SecretKey key = keygenerator.generateKey();
		return key;
	}
	
	private String hashSHA(byte[] bytesCode) {
		try {
			MessageDigest hasher = MessageDigest.getInstance(HASH_ALGORITHM);
			byte[] hash = hasher.digest(bytesCode);
			StringBuilder hexString = new StringBuilder(2 * hash.length);
		    for (int i = 0; i < hash.length; i++) {
		        String hex = Integer.toHexString(0xff & hash[i]);
		        if(hex.length() == 1) {
		            hexString.append('0');
		        }
		        hexString.append(hex);
		    }
		    return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			logger.log(Level.WARNING, "Algorithm invalid");
			System.exit(1);
			return "";
		}
	}
	
		
	private void signIt() {
		signature = hashSHA(this.key.getEncoded());
		stateManager.signit(signature);
	}

	public String encryptByte(byte[] messageBytes) {
		Cipher cipher;
		String encryptedMsg = "";
		try {
			cipher = Cipher.getInstance(SYNC_ENCRYPTION_ALGORITHM);
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
			cipher = Cipher.getInstance(SYNC_ENCRYPTION_ALGORITHM);
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
		System.out.println(encodeToBase64(key.getEncoded()));
		String base64 = this.encryptByte(key.getEncoded(), serverPublicKey);
		key = null;
		return base64;
	}

	public void configureDecryptMode(String key) throws InvalidKeyException{
		byte[] dataKey = decodeToBase64(key);
		String signature = hashSHA(dataKey);
		if(this.signature.equals(signature))
			this.key = new SecretKeySpec(dataKey,SYNC_ENCRYPTION_ALGORITHM);
		else
			throw new InvalidKeyException("The entered key is invalid");
	}

}
