package beaform.debug;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncDebugUtils {
	private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

	public static Future<?> clearDb() {
		return EXECUTOR.submit(new ClearDbTask());
	}

	public static Future<?> listAllFormulas() {
		return EXECUTOR.submit(new ListFormulasTask());
	}

	public static Future<?> fillDb() {
		return EXECUTOR.submit(new FillDbTask());
	}
}
