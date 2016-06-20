package beaform;

import java.util.List;
import java.util.concurrent.Callable;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import beaform.entities.Formula;
import beaform.entities.FormulaDAO;

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
	 */
	@Override
	public List<Formula> call() throws NotSupportedException, SystemException {
		final String name = this.tagName;

		final FormulaDAO formulaDAO = new FormulaDAO();
		return formulaDAO.findFormulasByTag(name);

	}
}