package aluka.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import aluka.enums.State;

public class StateManager {

	private HashMap<String, Integer> properties;
	private File file;

	public StateManager(String statePath) {
		file = new File(statePath);
		if (file.exists()) {
			try {
				loadMap();
			} catch (ClassNotFoundException e) {
				properties = new HashMap<String, Integer>();
				file.deleteOnExit();
			}
		} else {
			properties = new HashMap<String, Integer>();
		}
	}

	public synchronized void saveState(HashMap<String,Integer> args) {
		for(String key :args.keySet()) {
			if(key.equals(State.ENCRYPTED.name())) {
				properties.put(key, args.get(key));
			}else if(key.equals(State.PRIVATE_KEY_PUSHED.name())) {
				properties.put(key, args.get(key));
			}
		}
		saveMap();
	}

	public synchronized void removeState(HashMap<String,Integer> args) {

		for(String key :args.keySet()) {
			if(key.equals(State.ENCRYPTED.name())) {
				properties.remove(key);
			}else if(key.equals(State.PRIVATE_KEY_PUSHED.name())) {
				properties.remove(key);
			}
		}
		saveMap();

	}

	private synchronized void saveMap() {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		while (true) {
			ObjectOutputStream objectWriter = null;
			try {
				objectWriter = new ObjectOutputStream(new FileOutputStream(file));
				objectWriter.writeObject(properties);
				break;
			} catch (FileNotFoundException e) {
				System.err.println("[ - ] - File Not found at saving");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("[ - ] - Error in writing");
			} finally {
				try {
					objectWriter.close();
				} catch (IOException e) {
					System.err.println("[ - ] - Error in closing");
					e.printStackTrace();
				}
			}
		}
	}

	private synchronized void loadMap() throws ClassNotFoundException {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		while (true) {
			ObjectInputStream objectReader = null;
			try {
				objectReader = new ObjectInputStream(new FileInputStream(file));
				this.properties = (HashMap<String, Integer>) objectReader.readObject();
				break;
			} catch (FileNotFoundException e) {
				System.err.println("[ - ] - File Not found at saving");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("[ - ] - Error in writing");
			} finally {
				try {
					objectReader.close();
				} catch (IOException e) {
					System.err.println("[ - ] - Error in closing");
					e.printStackTrace();
				}
			}
		}
	}
	
	public HashMap<String,Integer> getStates(){
		return properties;
	}

}
