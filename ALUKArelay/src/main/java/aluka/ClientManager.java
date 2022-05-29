package aluka;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientManager implements Runnable {

	private final static short TAMPON = 16;
	private Socket socket;
	private ShortServer shortServer;
	private short id;
	
	public ClientManager(Socket socket,ShortServer shortServer,short id) {
		this.socket = socket;
		this.shortServer = shortServer;
		this.id = id;
	}

	@Override
	public void run() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
			while (!this.socket.isClosed()) {
				String os = reader.readLine();
				String path = "";
				if (os.equals("windows")) {
					path = "windows.zip";
				} else if (os.equals("linux")) {
					path = "linux.zip";
				} else {
					path = "macos.zip";
				}

				File file = new File(path);
				try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))){
					byte[] binaries = new byte[TAMPON];
					while(bis.read(binaries) > 0) {
						dos.write(binaries);
					}
				}catch (Exception e) {
					System.err.println("Can not read file " + os);
					System.exit(1);
				}
			}
		} catch (IOException e) {
			System.out.println("[ - ] - Client disconnected");
			shortServer.disconnect(id);
		}

	}

}
