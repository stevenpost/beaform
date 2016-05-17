package beaform;

import java.util.concurrent.Callable;

import javax.persistence.PersistenceException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import beaform.entities.Formula;
import beaform.entities.FormulaDAO;

public final class SearchFormulaTask implements Callable<Formula> {

	private final String name;

	public SearchFormulaTask(final String searchForName) {
		this.name = searchForName;
	}

	@Override
	public Formula call() throws NotSupportedException, SystemException {

		final FormulaDAO formulaDAO = new FormulaDAO();

		final TransactionManager tm = GraphDbHandlerForJTA.getInstance().getTransactionManager();

		tm.begin();
		Formula result;

		try {
			result = formulaDAO.findFormulaByName(this.name);
			try {
				tm.commit();
			}
			catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
							| HeuristicRollbackException | SystemException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch (PersistenceException pe) {
			tm.rollback();
			throw pe;
		}

		return result;

	}
}