package beaform.debug;

/**
 * This class implements a task to print all formulas.
 *
 * @author steven
 *
 */
public class ListFormulasTask implements Runnable {

	/**
	 * This method is invoked when the task starts.
	 */
	@Override
	public void run() {
		DebugUtils.listAllFormulas();
	}
}
