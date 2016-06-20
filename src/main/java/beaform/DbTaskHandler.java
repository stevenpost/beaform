package beaform;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class handles database searches.
 *
 * @author Steven Post
 *
 */
public final class DbTaskHandler {

	/** An executor service for handling DB tasks */
	private static final ExecutorService EXEC_SERVICE = Executors.newSingleThreadExecutor();

	private DbTaskHandler() {
		// Utility classes don't have constructors.
	}

	/**
	 * Adds a task to be executed and provides a {@link Future}.
	 * @param task The task to be executed.
	 * @return The {@link Future} for this task.
	 */
	public static <T> Future<T> addTask(final Callable<T> task) {
		return EXEC_SERVICE.submit(task);
	}

	/**
	 * Adds a task to be executed and provides a {@link Future}.
	 * @param task The task to be executed.
	 * @return The {@link Future} for this task.
	 */
	@SuppressWarnings("unchecked")
	public static Future<Void> addTask(final Runnable task) {
		return (Future<Void>) EXEC_SERVICE.submit(task);
	}

}
