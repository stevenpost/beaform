package beaform.search;

import java.util.concurrent.Callable;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import beaform.dao.FormulaDAO;
import beaform.entities.Formula;

/**
 * A search task to find formulas.
 *
 * @author Steven Post
 *
 */
public final class SearchFormulaTask implements Callable<Formula> {

	private final String name;

	public SearchFormulaTask(final String searchForName) {
		this.name = searchForName;
	}

	@Override
	public Formula call() throws NotSupportedException, SystemException {
		return FormulaDAO.findFormulaByName(this.name);
	}

}
