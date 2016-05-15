package beaform;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import beaform.entities.Formula;

public final class SearchFormulaTask implements Callable<Formula> {

	private final String name;

	public SearchFormulaTask(String searchForName) {
		this.name = searchForName;
	}

	@Override
	public Formula call() throws NotSupportedException, SystemException {

		TransactionManager tm = GraphDbHandlerForJTA.getInstance().getTransactionManager();

		tm.begin();
		Formula result;

		try {
			final EntityManager em = GraphDbHandlerForJTA.getInstance().getEntityManagerFactory().createEntityManager();

			String query = "match (n:Formula { name:'" + this.name + "' }) return n";
			result = (Formula) em.createNativeQuery(query, Formula.class).getSingleResult();
			System.out.println("Found: " + result);

			System.out.println("Printing ingredients...");
			Iterator<Entry<String, Formula>> it = result.getIngredients();
			while (it.hasNext()) {
				Entry<String, Formula> entry = it.next();

				String amount = entry.getKey();
				amount = amount.substring(amount.indexOf('|') + 1);
				System.out.println(" - " + amount + " " + entry.getValue());
			}

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