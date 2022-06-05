package onion.aluka.datas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TargetService {

	@Autowired
	private static TargetRepository targetRepository;
	
	public static synchronized  void saveTarget(Target target) {
		targetRepository.save(target);
	}
}
