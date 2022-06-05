package onion.aluka;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import onion.aluka.core.ServerAluka;
import onion.aluka.datas.Target;
import onion.aluka.datas.TargetRepository;

@SpringBootApplication
public class AlukAserverApplication implements CommandLineRunner {
	
	@Autowired
	public static TargetRepository targetRepository;
	
	public static boolean isRunning = true;
	
	
	public static void main(String[] args) {
		SpringApplication.run(AlukAserverApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ServerAluka server = ServerAluka.getInstance();
		Thread thread = new Thread(server);
		thread.start();
	}
	
	public static synchronized  void saveTarget(Target target) {
		targetRepository.save(target);
	}

}
