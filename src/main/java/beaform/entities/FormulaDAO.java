package beaform.entities;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
import beaform.Ingredient;
import beaform.SearchTagTask;

/**
 * This class handles all DB access for formulas.
 *
 * @author steven
 *
 */
public class FormulaDAO {

	/**
	 * The logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FormulaDAO.class);

	/**
	 * Get the ingredients of a formula.
	 * @param formula
	 * @return a list of {@link Ingredient} objects
	 * @throws NotSupportedException
	 * @throws SystemException
	 */
	public List<Ingredient> getIngredients(Formula formula) throws NotSupportedException, SystemException {
		final TransactionManager transactionMgr = GraphDbHandlerForJTA.getInstance().getTransactionManager();

		transactionMgr.begin();

		final EntityManager ementityManager = GraphDbHandlerForJTA.getInstance().getEntityManagerFactory().createEntityManager();
		formula = (Formula) ementityManager.createNativeQuery("match (n:Formula { name:'" + formula.getName() + "' }) return n", Formula.class).getSingleResult();
		final List<Ingredient> retList = formula.getIngredients();

		ementityManager.flush();
		ementityManager.close();

		try {
			transactionMgr.commit();
		}
		catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e1)
		{
			LOG.error(e1.getMessage(), e1);
		}

		return retList;
	}

	/**
	 * Update an existing formula.
	 *
	 * @param oldName the old name of the formula
	 * @param name the name of the formula
	 * @param description the description for the formula
	 * @param totalAmount the total amount for the formula
	 * @param ingredients a list of ingredients
	 * @param tags a list of tags
	 */
	public void updateExisting(final String name, final String description, final String totalAmount, final List<Ingredient> ingredients, final List<FormulaTag> tags) {
		final TransactionManager tmtransactionMgr = GraphDbHandlerForJTA.getInstance().getTransactionManager();

		try {
			tmtransactionMgr.begin();
		}
		catch (NotSupportedException | SystemException e1) {
			LOG.error(e1.getMessage(), e1);
			return;
		}

		final EntityManager ementityManager = GraphDbHandlerForJTA.getInstance().getEntityManagerFactory().createEntityManager();

		final String query = "match (n:Formula { name:'" + name + "' }) return n";
		final Formula formula = (Formula) ementityManager.createNativeQuery(query, Formula.class).getSingleResult();

		formula.setDescription(description);
		formula.setTotalAmount(totalAmount);
		formula.clearTags();

		addTags(tags, ementityManager, formula);

		formula.clearIngredients();
		for (final Ingredient ingredient : ingredients) {
			// We should only be holding existing Formulas at this point
			formula.addIngredient(ingredient.getFormula(), ingredient.getAmount());
		}

		ementityManager.persist(formula);

		ementityManager.flush();
		ementityManager.close();

		try {
			tmtransactionMgr.commit();
		}
		catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e1)
		{
			LOG.error(e1.getMessage(), e1);
		}
	}

	public void addFormula(final String name, final String description, final String totalAmount, final List<Ingredient> ingredients, final List<FormulaTag> tags) {
		final TransactionManager transactionMgr = GraphDbHandlerForJTA.getInstance().getTransactionManager();

		try {
			transactionMgr.begin();
		}
		catch (NotSupportedException | SystemException e1) {
			LOG.error(e1.getMessage(), e1);
			return;
		}

		final EntityManager entityManager = GraphDbHandlerForJTA.getInstance().getEntityManagerFactory().createEntityManager();

		final Formula formula = new Formula();
		formula.setName(name);
		formula.setDescription(description);
		formula.setTotalAmount(totalAmount);

		addTags(tags, entityManager, formula);

		for (final Ingredient ingredient : ingredients) {
			// We should only be holding existing Formulas at this point
			formula.addIngredient(ingredient.getFormula(), ingredient.getAmount());
		}

		entityManager.persist(formula);

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
	}

	/**
	 * This method adds tags to a formula.
	 *
	 * @param tags A list of tags
	 * @param em an open entity manager
	 * @param formula the formula to add the tags to
	 */
	private void addTags(final List<FormulaTag> tags, final EntityManager em, final Formula formula) {
		for (FormulaTag tag : tags) {
			// See if the tag exist in the DB, if so, use it.
			FormulaTag pTag = null;
			final Future<FormulaTag> searchresult = GraphDbHandlerForJTA.addTask(new SearchTagTask(tag.getName()));
			try {
				pTag = searchresult.get();
			}
			catch (InterruptedException | ExecutionException e1) {
				LOG.error(e1.getMessage(), e1);
			}
			if (pTag == null) {
				em.persist(tag);
			}
			else {
				tag = pTag;
			}
			formula.addTag(tag);
		}
	}

}
