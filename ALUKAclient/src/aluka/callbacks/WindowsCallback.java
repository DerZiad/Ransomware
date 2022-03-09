package aluka.callbacks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class WindowsCallback implements Supplier<List<String>>{

	@Override
	public List<String> get() {
		return Arrays.asList("WINDOWS");
	}

}
