package beaform;

import java.util.List;
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
			List<Ingredient> ingredients = result.getIngredients();
			for (Ingredient ingredient : ingredients) {
				String amount = ingredient.getAmount();
				System.out.println(" - " + amount + " " + ingredient.getFormula().getName());
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