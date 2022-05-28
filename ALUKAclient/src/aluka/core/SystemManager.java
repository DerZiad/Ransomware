package aluka.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.function.Supplier;

import aluka.configuration.Configuration;

class DownloadTor implements Runnable {

	private String targetDownload;
	private String torUrl;
	private Supplier<String> callback;

	public DownloadTor(String targetDownload, String torUrl, Supplier<String> callback) {
		super();
		this.targetDownload = targetDownload;
		this.torUrl = torUrl;
		this.callback = callback;
	}

	@Override
	public void run() {
		System.out.println("[ + ] - Installing tor browser");
		
	}

}

public class SystemManager {

	private Supplier<List<String>> callback;

	private String state;
	
	public SystemManager() {
		System.out.println("[ + ] - Scanning System");
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			callback = Configuration.getWindowsCallback();
			state = "pwned";
		} else if (os.contains("osx")) {
			callback = Configuration.getMacosCallback();
			this.state = "/tmp/pwned";
		} else if (os.contains("nix") || os.contains("aix") || os.contains("nux")) {
			callback = Configuration.getLinuxCallback();
			this.state = "/tmp/pwned";
		} else {
			callback = Configuration.getWindowsCallback();
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
		/*
		 * File[] paths = File.listRoots(); String[] startPaths = new
		 * String[paths.length]; for (int i = 0; i < startPaths.length; i++) {
		 * startPaths[i] = paths[i].getAbsolutePath(); } return startPaths;
		 */
	}
	
	public String getConfigPath() {
		return state;
	}
}
