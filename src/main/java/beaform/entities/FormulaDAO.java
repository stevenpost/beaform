package beaform.entities;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
	public List<Ingredient> getIngredients(final Formula formula) throws NotSupportedException, SystemException {

		final boolean hasTransaction = setupTransaction();

		final EntityManager entityManager = GraphDbHandlerForJTA.getNewEntityManager();
		final Formula retrievedFormula = findByName(formula.getName(), entityManager);
		final List<Ingredient> retList = retrievedFormula.getIngredients();

		GraphDbHandlerForJTA.tryCloseEntityManager(entityManager);

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

		final EntityManager entityManager = GraphDbHandlerForJTA.getNewEntityManager();

		final Formula formula = findByName(name, entityManager);

		setFormulaProperties(formula, description, totalAmount);

		clearFormulaRelations(formula);

		addTags(tags, entityManager, formula);
		addIngredientsToFormula(formula, ingredients);

		entityManager.persist(formula);

		GraphDbHandlerForJTA.tryCloseEntityManager(entityManager);

		if (hasTransaction) {
			commitTransation();
		}
	}

	/**
	 * This method adds ingredients to a formula.
	 * @param formula the formula
	 * @param ingredients a list of ingredients to add
	 */
	private void addIngredientsToFormula(final Formula formula, final List<Ingredient> ingredients) {
		for (final Ingredient ingredient : ingredients) {
			// We should only be holding existing Formulas at this point
			formula.addIngredient(ingredient.getFormula(), ingredient.getAmount());
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
		setFormulaProperties(formula, name, description, totalAmount);

		addTags(tags, entityManager, formula);

		addIngredientsToFormula(formula, ingredients);

		entityManager.persist(formula);

		GraphDbHandlerForJTA.tryCloseEntityManager(entityManager);

		if (hasTransaction) {
			commitTransation();
		}
	}

	/**
	 * This method sets the different properties of a formula.
	 * @param formula The formula to change.
	 * @param name The name of the formula.
	 * @param description The description of the formula.
	 * @param totalAmount The total amount in this formula.
	 */
	private void setFormulaProperties(final Formula formula, final String description, final String totalAmount) {
		formula.setDescription(description);
		formula.setTotalAmount(totalAmount);
	}

	/**
	 * This method sets the different properties of a formula.
	 * @param formula The formula to change.
	 * @param name The name of the formula.
	 * @param description The description of the formula.
	 * @param totalAmount The total amount in this formula.
	 */
	private void setFormulaProperties(final Formula formula, final String name, final String description, final String totalAmount) {
		formula.setName(name);
		formula.setDescription(description);
		formula.setTotalAmount(totalAmount);
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

		for (final FormulaTag tag : tags) {
			addTagToFormula(entityManager, formula, formulaTagDAO, tag);
		}
	}

	/**
	 * Add a tag to a formula.
	 *
	 * If the tag does not yet exist in the DB it will be added.
	 *
	 * @param entityManager the entity manager to use
	 * @param formula the formula to add the tag to
	 * @param formulaTagDAO the DAO for internal lookups
	 * @param tag the tag to add
	 */
	private void addTagToFormula(final EntityManager entityManager, final Formula formula,
	                             final FormulaTagDAO formulaTagDAO, final FormulaTag tag) {
		// See if the tag exist in the DB, if so, use it.
		FormulaTag tagToAdd;
		try {
			tagToAdd = formulaTagDAO.findByObject(tag);
		}
		catch (NotSupportedException | SystemException e1) {
			LOG.error("Failed to find the tag", e1);
			return;
		}
		catch (NoResultException e1) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("No tag with name " + tag.getName() + " found: " + e1.getMessage());
			}
			entityManager.persist(tag);
			tagToAdd = tag;
		}
		formula.addTag(tagToAdd);
	}

	/**
	 * This method finds a formula in the DB based on a name.
	 * It assumes a transaction is already in progress.
	 *
	 * @param name the name of the formula to look for
	 * @return the found {@link Formula} or null if none was found.
	 * @throws NotSupportedException
	 * @throws SystemException
	 */
	public Formula findFormulaByName(final String name) throws SystemException, NotSupportedException {

		final boolean hasTransaction = setupTransaction();

		final EntityManager entityManager = GraphDbHandlerForJTA.getNewEntityManager();

		final Formula result = findByName(name, entityManager);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Found: " + result);
		}

		GraphDbHandlerForJTA.tryCloseEntityManager(entityManager);

		if (hasTransaction) {
			commitTransation();
		}

		return result;
	}

	/**
	 * Find formulas based on a tag.
	 *
	 * @param tagName The name of the tag.
	 * @return a list of matching formulas
	 * @throws NotSupportedException
	 * @throws SystemException
	 */
	public List<Formula> findFormulasByTag(final String tagName) throws SystemException, NotSupportedException {
		final boolean hasTransaction = setupTransaction();

		final EntityManager entityManager = GraphDbHandlerForJTA.getNewEntityManager();

		final List<Formula> result = findByTag(tagName, entityManager);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Found: " + result);
		}

		GraphDbHandlerForJTA.tryCloseEntityManager(entityManager);

		if (hasTransaction) {
			commitTransation();
		}

		return result;
	}

	/**
	 * Find formulas that are tagged by a specific tag.
	 * @param tagName the tag
	 * @param entityManager the entity manager
	 * @return the list of formulas found
	 */
	private List<Formula> findByTag(final String tagName, final EntityManager entityManager) {
		final String query = "MATCH (t:FormulaTag { name:'" + tagName + "' })<-[r]-(f:Formula) RETURN f";

		@SuppressWarnings("unchecked")
		final List<Formula> result = entityManager.createNativeQuery(query, Formula.class).getResultList();

		return result;
	}

	private Formula findByName(final String name, final EntityManager entityManager) {
		final String query = "match (n:Formula { name:'" + name + "' }) return n";
		final Formula result = (Formula) entityManager.createNativeQuery(query, Formula.class).getSingleResult();

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

	private void clearFormulaRelations(final Formula formula) {
		formula.clearIngredients();
		formula.clearTags();
	}

}
