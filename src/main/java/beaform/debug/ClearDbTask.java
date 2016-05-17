package beaform.debug;

/**
 * A task that clears the DB.
 *
 * @author Steven Post
 *
 */
public class ClearDbTask implements Runnable {

	/**
	 * Invoked when the task is executed.
	 */
	@Override
	public void run() {
		DebugUtils.clearDb();
	}
}