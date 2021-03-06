package beaform.debug;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class AsyncDebugUtils {

	private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

	private AsyncDebugUtils() {
		// Utility classes don't need public constructors
	}

	public static Future<String> clearDb() {
		return EXECUTOR.submit(new ClearDbTask(), "Done");
	}

	public static Future<String> listAllFormulas() {
		return EXECUTOR.submit(new ListFormulasTask(), "Done");
	}

	public static Future<String> fillDb() {
		return EXECUTOR.submit(new FillDbTask(), "Done");
	}
}
