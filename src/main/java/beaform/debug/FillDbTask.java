package beaform.debug;

/**
 * A task to fill the DB with test data.
 *
 * @author Steven Post
 *
 */
public final class FillDbTask implements Runnable {

	/**
	 * Invoked when the task executes.
	 */
	@Override
	public void run() {
		DebugUtils.fillDb();
	}
}
