package aluka.destructors;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import javax.crypto.SecretKey;

import aluka.core.LoggerManagement;
import aluka.core.Utils;
import aluka.exception.FileTouchedException;

public class DestructionManager {
	
	//Constante
	LoggerManagement logger = LoggerManagement.getInstance();
	
	private static DestructionManager instance;
	
	private static final String CONFIGURATION_PATH = "";
	
	private static final String KEY = "KEY";
	
	//Variables
	private Properties properties;
	private SecretKey key;
	
	
	private DestructionManager() throws FileTouchedException {
		properties = new Properties();
		logger.log(Level.INFO, "Reading KeySpec");
		File keyFile = new File(CONFIGURATION_PATH);
		if(keyFile.exists()) {
			try {
				Utils.readFileContent(CONFIGURATION_PATH);
			} catch (IOException e) {
				throw new FileTouchedException("Secret Key file can not be readed");
			}
		}
			
		
	}
	
	
	
	public static DestructionManager getInstance() throws FileTouchedException {
		if(instance == null)
			instance = new DestructionManager();
		return instance;
	}
	
	public void clean() {
		
	}
	
	
}
