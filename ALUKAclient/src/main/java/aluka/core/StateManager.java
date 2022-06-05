package aluka.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

public class StateManager {

	// Constantes
	private final static String CONFIG_PATH = "configuration.aluka";
	private final static String PWNED_CONST = "pwned";
	private final static String TOR_READY_CONST = "tor";
	private final static String KEY_EXPORTED_CONST = "exported";
	private final static String ENCRYPTED_CONST = "encrypted";

	// Variables
	private Properties properties;
	private static StateManager instance;
	private LoggerManagement logger = LoggerManagement.getInstance();
	private static File configuration = new File(CONFIG_PATH);

	public StateManager() {
		configuration = new File(CONFIG_PATH);
		properties = new Properties();
		if (configuration.exists()) {
			try {
				logger.log(Level.INFO, "Loading configs");
				properties.load(new FileInputStream(configuration));
			} catch (Exception e) {
				createConfigFile();
			}
		} else
			createConfigFile();
		unmarkPwned();
		unmarkTor();
		unmarkExported();
	}

	private void createConfigFile() {
		try {
			logger.log(Level.INFO, "Creating configuration file");
			configuration.createNewFile();
			save();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Can t create a config file", e);
			System.exit(1);
		}
	}

	public static StateManager getInstance() {
		if (instance == null)
			instance = new StateManager();
		return instance;
	}

	public void markPwned() {
		logger.log(Level.INFO,"Marking Pwned");
		properties.put(PWNED_CONST, "1");
		save();
	}

	public void unmarkPwned() {
		logger.log(Level.INFO,"Unmarking Pwned");
		properties.put(PWNED_CONST, "0");
		save();
	}

	public void markTor() {
		logger.log(Level.INFO,"Marking Tor");
		properties.put(TOR_READY_CONST, "1");
		save();
	}

	public void unmarkTor() {
		logger.log(Level.INFO,"Unmarking Tor");
		properties.put(TOR_READY_CONST, "0");
		save();
	}

	public void markExported() {
		logger.log(Level.INFO,"Marking Key Exported");
		properties.put(KEY_EXPORTED_CONST, "1");
		save();
	}

	public void unmarkExported() {
		logger.log(Level.INFO,"Unmarking Key Exported");
		properties.put(KEY_EXPORTED_CONST, "0");
		save();
	}
	
	public void markEncrypted() {
		logger.log(Level.INFO,"Marking Key Exported");
		properties.put(ENCRYPTED_CONST, "1");
		save();
	}
	
	public void unmarkEncrypted() {
		logger.log(Level.INFO,"Marking Encrypted");
		properties.put(ENCRYPTED_CONST, "0");
		save();
	}
	
	public boolean isEncrypted() {
		return properties.get(ENCRYPTED_CONST).equals("1");
	}
	
	public boolean isPwned() {
		return properties.get(PWNED_CONST).equals("1");
	}

	public boolean isExported() {
		return properties.get(KEY_EXPORTED_CONST).equals("1");
	}

	public boolean isTorInstalled() {
		return properties.get(TOR_READY_CONST).equals("1");
	}

	@SuppressWarnings("deprecation")
	private synchronized void save() {
		logger.log(Level.INFO,"Saving instance");
		try {
			properties.save(new FileOutputStream(configuration), new String());
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Can t save state",e);
		}
	}

}
