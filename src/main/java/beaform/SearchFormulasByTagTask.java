package beaform;

import java.util.List;
import java.util.concurrent.Callable;

import beaform.dao.FormulaDAO;
import beaform.entities.Formula;
import beaform.entities.TransactionSetupException;

/**
 * A search task to find formulas, based on tags assigned to them.
 *
 * @author Steven Post
 *
 */
public final class SearchFormulasByTagTask implements Callable<List<Formula>> {

	/**
	 * The tag to search for
	 */
	private final String tagName;

	/**
	 * Constructor.
	 *
	 * @param tagName the tag
	 */
	public SearchFormulasByTagTask(final String tagName) {
		this.tagName = tagName;
	}

	/**
	 * Called when the task is executed.
	 *
	 * @return the found formula, or null if none was found
	 * @throws TransactionSetupException
	 */
	@Override
	public List<Formula> call() throws TransactionSetupException {
		final String name = this.tagName;

		return FormulaDAO.findFormulasByTag(name);

	}
}
