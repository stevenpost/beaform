package beaform.debug;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.GraphDbHandlerForJTA;
import beaform.entities.Formula;

/**
 * This class implements a task to retrieve all formulas.
 *
 * @author steven
 *
 */
public class ListFormulasTask implements Runnable {

	/**
	 * The logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ListFormulasTask.class);

	@Override
	public void run() {
		final TransactionManager transactionMgr = GraphDbHandlerForJTA.getInstance().getTransactionManager();

		try {
			transactionMgr.begin();
		}
		catch (NotSupportedException | SystemException e1) {
			LOG.error(e1.getMessage(), e1);
			return;
		}

		final EntityManager entityManager = GraphDbHandlerForJTA.getInstance().createNewEntityManager();

		final String query = "match (n:Formula) return n";

		@SuppressWarnings("unchecked")
		final List<Formula> formulas = entityManager.createNativeQuery(query, Formula.class).getResultList();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Number of formulas: " + formulas.size());
		}

		for (final Formula formula : formulas) {
			LOG.debug(formula.toString());
		}
		entityManager.flush();
		entityManager.close();

		try {
			transactionMgr.commit();
		}
		catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e1)
		{
			LOG.error(e1.getMessage(), e1);
		}

		LOG.debug(query);
	}
}