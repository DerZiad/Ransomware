package aluka.callbacks;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class LinuxCallback implements Supplier<List<String>>{

	@Override
	public List<String> get() {
		return Arrays.asList("/dev","/boot");
	}
	
}
