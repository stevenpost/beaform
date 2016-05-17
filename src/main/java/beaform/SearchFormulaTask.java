package beaform;

import java.util.concurrent.Callable;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import beaform.entities.Formula;
import beaform.entities.FormulaDAO;

public final class SearchFormulaTask implements Callable<Formula> {

	/**
	 * The name of the formula to search for
	 */
	private final transient String name;

	public SearchFormulaTask(final String searchForName) {
		this.name = searchForName;
	}

	@Override
	public Formula call() throws NotSupportedException, SystemException {

		final FormulaDAO formulaDAO = new FormulaDAO();

		Formula result = formulaDAO.findFormulaByName(this.name);


		return result;

	}
}