package aluka;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerRelay {

	public ServerRelay() {
		try {
			ServerSocket server = new ServerSocket(8989);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
