package beaform.entities;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
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
public final class FormulaDAO {

	/**
	 * The logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FormulaDAO.class);

	/** Query to search for a formula by name */
	private static final String FORMULA_BY_NAME = "match (n:Formula { name:{name} }) return n";

	/** Query to search for a formula by tag */
	private static final String FORMULA_BY_TAG = "MATCH (t:FormulaTag { name:{name} })<-[r]-(f:Formula) RETURN f";

	private FormulaDAO() {
		// private constructor, because this is a utility class.
	}

	/**
	 * Get the ingredients of a formula.
	 *
	 * @param formula
	 * @return a list of {@link Ingredient} objects
	 * @throws TransactionSetupException if the transaction cannot be set up
	 */
	public static List<Ingredient> getIngredients(final Formula formula) throws TransactionSetupException {

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
	 * @throws TransactionSetupException if the transaction cannot be set up
	 */
	public static void updateExisting(final String name,
	                                  final String description,
	                                  final String totalAmount,
	                                  final List<Ingredient> ingredients,
	                                  final List<FormulaTag> tags)
	                                				  throws TransactionSetupException {

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
	private static void addIngredientsToFormula(final Formula formula, final List<Ingredient> ingredients) {
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
	 * @throws TransactionSetupException if the transaction cannot be set up
	 */
	public static void addFormula(final String name,
	                              final String description,
	                              final String totalAmount,
	                              final List<Ingredient> ingredients,
	                              final List<FormulaTag> tags)
	                            				  throws TransactionSetupException {

		final boolean hasTransaction = setupTransaction();

		final EntityManager entityManager = GraphDbHandlerForJTA.getNewEntityManager();

		final Formula formula = new Formula(name, description, totalAmount);

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
	private static void setFormulaProperties(final Formula formula, final String description, final String totalAmount) {
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
	private static void addTags(final List<FormulaTag> tags, final EntityManager entityManager, final Formula formula) {

		for (final FormulaTag tag : tags) {
			addTagToFormula(entityManager, formula, tag);
		}
	}

	/**
	 * Add a tag to a formula.
	 *
	 * If the tag does not yet exist in the DB it will be added.
	 *
	 * @param entityManager the entity manager to use
	 * @param formula the formula to add the tag to
	 * @param tag the tag to add
	 */
	private static void addTagToFormula(final EntityManager entityManager, final Formula formula,
	                                    final FormulaTag tag) {
		// See if the tag exist in the DB, if so, use it.
		FormulaTag tagToAdd;
		try {
			tagToAdd = FormulaTagDAO.findByObject(tag);
		}
		catch (NoResultException e1) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("No tag with name " + tag.getName() + " found: " + e1.getMessage(), e1);
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
	 * @throws TransactionSetupException if the transaction cannot be set up
	 */
	public static Formula findFormulaByName(final String name) throws TransactionSetupException {

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
	 * @throws TransactionSetupException
	 */
	public static List<Formula> findFormulasByTag(final String tagName) throws TransactionSetupException {
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
	private static List<Formula> findByTag(final String tagName, final EntityManager entityManager) {
		final Query query = entityManager.createNativeQuery(FORMULA_BY_TAG, Formula.class);
		query.setParameter("name", tagName);
		return query.getResultList();
	}

	private static Formula findByName(final String name, final EntityManager entityManager) {
		final Query query = entityManager.createNativeQuery(FORMULA_BY_NAME, Formula.class);
		query.setParameter("name", name);

		return (Formula) query.getSingleResult();
	}

	private static boolean setupTransaction() throws TransactionSetupException {
		final TransactionManager transactionMgr = GraphDbHandlerForJTA.getTransactionManager();
		try {
			if (GraphDbHandlerForJTA.getTransactionManagerStatus() == Status.STATUS_NO_TRANSACTION) {
				transactionMgr.begin();
				return true;
			}
		}
		catch (SystemException | NotSupportedException e) {
			throw new TransactionSetupException("Something went wrong setting up the transaction", e);
		}
		return false;
	}

	private static boolean commitTransation() {
		final TransactionManager transactionMgr = GraphDbHandlerForJTA.getTransactionManager();
		try {
			transactionMgr.commit();
		}
		catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e1) {
			LOG.error(e1.getMessage(), e1);
			return false;
		}
		return true;
	}

	private static void clearFormulaRelations(final Formula formula) {
		formula.clearIngredients();
		formula.clearTags();
	}

}
