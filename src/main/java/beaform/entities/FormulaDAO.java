package beaform.entities;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.GraphDbHandlerForJTA;
import beaform.Ingredient;

/**
 * This class handles all DB access for formulas.
 *
 * @author Steven Post
 *
 */
public class FormulaDAO {

	/**
	 * The logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FormulaDAO.class);

	/**
	 * Get the ingredients of a formula.
	 *
	 * @param formula
	 * @return a list of {@link Ingredient} objects
	 * @throws NotSupportedException If the calling thread is already
	 *         associated with a transaction,
	 *         and nested transactions are not supported.
	 * @throws SystemException If the transaction service fails in an unexpected way.
	 */
	public List<Ingredient> getIngredients(Formula formula) throws NotSupportedException, SystemException {

		final boolean hasTransaction = setupTransaction();

		final EntityManager ementityManager = GraphDbHandlerForJTA.getNewEntityManager();
		formula = (Formula) ementityManager.createNativeQuery("match (n:Formula { name:'" + formula.getName() + "' }) return n", Formula.class).getSingleResult();
		final List<Ingredient> retList = formula.getIngredients();

		ementityManager.flush();
		ementityManager.close();

		if (hasTransaction) {
			commitTransation();
		}

		return retList;
	}

	/**
	 * Update an existing formula.
	 *
	 * @param name the name of the formula
	 * @param description the description for the formula
	 * @param totalAmount the total amount for the formula
	 * @param ingredients a list of ingredients
	 * @param tags a list of tags
	 *
	 * @throws NotSupportedException If the calling thread is already
	 *         associated with a transaction,
	 *         and nested transactions are not supported.
	 * @throws SystemException If the transaction service fails in an unexpected way.
	 */
	public void updateExisting(final String name, final String description, final String totalAmount, final List<Ingredient> ingredients, final List<FormulaTag> tags) throws SystemException, NotSupportedException {
		final boolean hasTransaction = setupTransaction();

		final EntityManager ementityManager = GraphDbHandlerForJTA.getNewEntityManager();

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

		if (hasTransaction) {
			commitTransation();
		}
	}

	/**
	 * Add a new formula.
	 *
	 * @param name the name of the formula
	 * @param description the description for the formula
	 * @param totalAmount the total amount for the formula
	 * @param ingredients a list of ingredients
	 * @param tags a list of tags
	 *
	 * @throws NotSupportedException If the calling thread is already
	 *         associated with a transaction,
	 *         and nested transactions are not supported.
	 * @throws SystemException If the transaction service fails in an unexpected way.
	 */
	public void addFormula(final String name, final String description, final String totalAmount, final List<Ingredient> ingredients, final List<FormulaTag> tags) throws SystemException, NotSupportedException {
		final boolean hasTransaction = setupTransaction();

		final EntityManager entityManager = GraphDbHandlerForJTA.getNewEntityManager();

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

		if (hasTransaction) {
			commitTransation();
		}
	}

	/**
	 * This method adds tags to a formula.
	 * It assumes a running transaction.
	 *
	 * @param tags A list of tags
	 * @param entityManager an open entity manager
	 * @param formula the formula to add the tags to
	 * @throws NotSupportedException If the calling thread is already
	 *         associated with a transaction,
	 *         and nested transactions are not supported.
	 * @throws SystemException If the transaction service fails in an unexpected way.
	 */
	private void addTags(final List<FormulaTag> tags, final EntityManager entityManager, final Formula formula) throws SystemException, NotSupportedException {
		final FormulaTagDAO formulaTagDAO = new FormulaTagDAO();

		for (FormulaTag tag : tags) {
			// See if the tag exist in the DB, if so, use it.
			FormulaTag pTag = null;
			try {
				pTag = formulaTagDAO.findByObject(tag);
			}
			catch (NotSupportedException | SystemException e1) {
				LOG.error("Failed to find the tag", e1);
			}
			if (pTag == null) {
				entityManager.persist(tag);
			}
			else {
				tag = pTag;
			}
			formula.addTag(tag);
		}
	}

	/**
	 * This method finds a formula in the DB based on a name.
	 * It assumes a transaction is already in progress.
	 *
	 * @param formula the name of the formula to look for
	 * @return the found {@link Formula} or null if none was found.
	 * @throws NotSupportedException
	 * @throws SystemException
	 */
	public Formula findFormulaByName(String name) throws SystemException, NotSupportedException {

		final boolean hasTransaction = setupTransaction();
		Formula result;

		final EntityManager em = GraphDbHandlerForJTA.getNewEntityManager();

		final String query = "match (n:Formula { name:'" + name + "' }) return n";
		result = (Formula) em.createNativeQuery(query, Formula.class).getSingleResult();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Found: " + result);
		}

		em.flush();
		em.close();

		if (hasTransaction) {
			commitTransation();
		}

		return result;
	}

	private boolean setupTransaction() throws SystemException, NotSupportedException {
		final TransactionManager transactionMgr = GraphDbHandlerForJTA.getTransactionManager();
		if (transactionMgr.getStatus() == Status.STATUS_NO_TRANSACTION) {
			transactionMgr.begin();
			return true;
		}
		return false;
	}

	private boolean commitTransation() {
		final TransactionManager transactionMgr = GraphDbHandlerForJTA.getTransactionManager();
		try {
			transactionMgr.commit();
		}
		catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e1)
		{
			LOG.error(e1.getMessage(), e1);
			return false;
		}
		return true;
	}

}
