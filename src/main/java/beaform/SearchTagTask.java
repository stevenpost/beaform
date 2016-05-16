package beaform;

import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import beaform.entities.FormulaTag;

public final class SearchTagTask implements Callable<FormulaTag> {

	private final String name;

	public SearchTagTask(final String searchForName) {
		this.name = searchForName;
	}

	@Override
	public FormulaTag call() throws NotSupportedException, SystemException {

		final TransactionManager tm = GraphDbHandlerForJTA.getInstance().getTransactionManager();

		tm.begin();
		FormulaTag result;

		try {
			final EntityManager em = GraphDbHandlerForJTA.getInstance().getEntityManagerFactory().createEntityManager();

			final String query = "match (n:Tag { name:'" + this.name + "' }) return n";
			result = (FormulaTag) em.createNativeQuery(query, FormulaTag.class).getSingleResult();
			System.out.println("Found: " + result);

			em.flush();
			em.close();

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