package onion.aluka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import onion.aluka.core.ServerAluka;
import onion.aluka.datas.TargetRepository;

@SpringBootApplication
@Configurable
public class AlukAserverApplication implements CommandLineRunner {
	
	@Autowired
	public TargetRepository targetRepository;
	
	public static boolean isRunning = true;
	
	public static void main(String[] args) {
		SpringApplication.run(AlukAserverApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ServerAluka server = ServerAluka.getInstance(targetRepository);
		Thread thread = new Thread(server);
		thread.start();
	}
	
}
