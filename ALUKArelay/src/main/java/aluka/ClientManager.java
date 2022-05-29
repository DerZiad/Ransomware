package aluka;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientManager implements Runnable {

	private final static short TAMPON = 8;
	private Socket socket;
	private SlaveServer shortServer;
	private short id;

	public ClientManager(Socket socket, SlaveServer shortServer, short id) {
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
				try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
					byte[] fileBytes = bis.readAllBytes();
					dos.write(fileBytes);
					dos.flush();
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("[ - ] - Can not read file " + os);
					System.exit(1);
				}
				socket.close();
				shortServer.disconnect(id);
			}
		} catch (IOException e) {
			System.out.println("[ - ] - Client disconnected");
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			shortServer.disconnect(id);
		}

	}

}
