package beaform;

import java.util.concurrent.Callable;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import beaform.entities.Formula;
import beaform.entities.FormulaDAO;

/**
 * A search task to find formulas.
 *
 * @author Steven Post
 *
 */
public final class SearchFormulaTask implements Callable<Formula> {

	/**
	 * The name of the formula to search for
	 */
	private final transient String name;

	/**
	 * Constructor.
	 *
	 * @param searchForName the name of the formula to search for
	 */
	public SearchFormulaTask(final String searchForName) {
		this.name = searchForName;
	}

	/**
	 * Called when the task is executed.
	 *
	 * @return the found formula, or null if none was found
	 */
	@Override
	public Formula call() throws NotSupportedException, SystemException {
		final String name = this.name;

		final FormulaDAO formulaDAO = new FormulaDAO();
		final Formula result = formulaDAO.findFormulaByName(name);

		return result;

	}
}