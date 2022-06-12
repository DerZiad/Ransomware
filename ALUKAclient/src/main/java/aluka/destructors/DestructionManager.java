package aluka.destructors;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import aluka.core.LoggerManagement;
import aluka.core.StateManager;
import aluka.exception.FileTouchedException;

public class DestructionManager {

	// Managers
	private static LoggerManagement logger = LoggerManagement.getInstance();
	private static StateManager stateManager = StateManager.getInstance();

	private static DestructionManager instance;

	private static String CONFIGURATION_PATH = "keySpec";

	// ATTRIBUTS
	private static final String ALGORITHM = "AES";
	private static final short BYTES_NUMBER = 256;
	private static final short TAMPON = 1024;

	// Variables
	private SecretKey key;

	private DestructionManager() throws FileTouchedException {

		logger.log(Level.INFO, "Reading KeySpec");
		File keyFile = new File(CONFIGURATION_PATH);
		if (keyFile.exists()) {
			try {
				readKey();
			} catch (IOException e) {
				throw new FileTouchedException("Secret Key file can not be readed");
			}
		} else {
			try {
				key = createAESKey();
			} catch (NoSuchAlgorithmException e1) {
				logger.log(Level.SEVERE, ALGORITHM + "is not defined");
				System.exit(1);
			}
			try {
				saveKey();
				stateManager.setNumber(encryptByte("0".getBytes()));
			} catch (IOException e) {
				throw new FileTouchedException("Secret Key file can not be saved");
				/*
				 * File is not touched by i don t know what to do if we don t have permission to
				 * create a file , better to change configuration path in this path TODO
				 */
			}

		}

	}

	// Private methods

	private void saveKey() throws IOException {
		File file = new File(CONFIGURATION_PATH);
		if (file.exists())
			file.delete();
		file.createNewFile();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		bos.write(key.getEncoded());
		bos.flush();
		bos.close();
	}

	private void readKey() throws IOException {
		File file = new File(CONFIGURATION_PATH);
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

		int l;
		byte[] tampon = new byte[TAMPON];

		String key = "";
		while ((l = bis.read(tampon)) > 0) {
			key += new String(tampon, 0, l);
		}
		bis.close();
		this.key = new SecretKeySpec(key.getBytes(), ALGORITHM);

	}

	private SecretKey createAESKey() throws NoSuchAlgorithmException {
		logger.log(Level.INFO, "Generating " + ALGORITHM + " " + BYTES_NUMBER + " KEY for destruction service");
		SecureRandom securerandom = new SecureRandom();
		KeyGenerator keygenerator = KeyGenerator.getInstance(ALGORITHM);
		keygenerator.init(256, securerandom);
		SecretKey key = keygenerator.generateKey();
		return key;
	}

	private String encodeToBase64(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}

	private byte[] decodeToBase64(String data) {
		return Base64.getDecoder().decode(data);
	}

	private Integer getIncrement() throws FileTouchedException {
		String number = stateManager.getNumber();
		number = decryptByte(number);
		try {
			int number1 = Integer.parseInt(number);
			return number1;
		} catch (NumberFormatException e) {
			throw new FileTouchedException("Configuration iteration touched");
		}
	}

	private void saveIncrement(Integer number) {
		String numberEncrypted = encryptByte(("" + number).getBytes());
		stateManager.setNumber(numberEncrypted);
	}

	public boolean canDestroy() throws FileTouchedException {
		return this.getIncrement() > 3;
	}

	public void increment() throws FileTouchedException {
		this.saveIncrement(this.getIncrement() + 1);
	}

	public void decrement() throws FileTouchedException {
		this.saveIncrement(this.getIncrement() - 1);
	}

	public static DestructionManager getInstance() throws FileTouchedException {
		if (instance == null)
			instance = new DestructionManager();
		return instance;
	}

	public static void clean() {

	}

	public String encryptByte(byte[] messageBytes) {
		logger.log(Level.INFO, "Encrypting with server Public Key");
		Cipher cipher;
		String encryptString = "";
		try {
			cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, this.key);
			byte[] encryptedMsg = cipher.doFinal(messageBytes);
			encryptString = encodeToBase64(encryptedMsg);
		} catch (GeneralSecurityException e) {
			logger.log(Level.SEVERE, "Can not encrypt with AES key");
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

}
