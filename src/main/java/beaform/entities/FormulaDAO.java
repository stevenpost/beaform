package beaform.entities;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import beaform.GraphDbHandlerForJTA;
import beaform.Ingredient;
import beaform.SearchTagTask;

public class FormulaDAO {

	public Iterator<Entry<String, Formula>> getIngredients(Formula formula) throws NotSupportedException, SystemException {
		TransactionManager tm = GraphDbHandlerForJTA.getInstance().getTransactionManager();

		tm.begin();

		final EntityManager em = GraphDbHandlerForJTA.getInstance().getEntityManagerFactory().createEntityManager();
		formula = (Formula) em.createNativeQuery("match (n:Formula { name:'" + formula.getName() + "' }) return n", Formula.class).getSingleResult();
		Iterator<Entry<String, Formula>> retIt = formula.getIngredients();

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

		return retIt;
	}

	public void updateExisting(String oldName, String name, String description, List<Ingredient> ingredients, List<Tag> tags) {
		TransactionManager tm = GraphDbHandlerForJTA.getInstance().getTransactionManager();

		try {
			tm.begin();
		}
		catch (NotSupportedException | SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

		final EntityManager em = GraphDbHandlerForJTA.getInstance().getEntityManagerFactory().createEntityManager();

		String query = "match (n:Formula { name:'" + oldName + "' }) return n";
		Formula formula = (Formula) em.createNativeQuery(query, Formula.class).getSingleResult();

		formula.setName(name);
		formula.setDescription(description);
		formula.clearTags();

		for (Tag tag : tags) {
			// See if the tag exist in the DB, if so, use it.
			Tag pTag = null;
			final Future<Tag> searchresult = GraphDbHandlerForJTA.addTask(new SearchTagTask(tag.getName()));
			try {
				pTag = searchresult.get();
			}
			catch (InterruptedException | ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (pTag == null) {
				em.persist(tag);
				pTag = tag;
			}
			else {
				tag = pTag;
			}
			formula.addTag(tag);
		}

		formula.clearIngredients();
		for (Ingredient ingredient : ingredients) {
			// We should only be holding existing Formulas at this point
			formula.addIngredient(ingredient.getFormula(), ingredient.getAmount());
		}

		em.persist(formula);

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

}
