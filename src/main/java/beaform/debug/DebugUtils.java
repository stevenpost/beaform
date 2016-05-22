package beaform.debug;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.hibernate.ogm.exception.EntityAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.GraphDbHandlerForJTA;
import beaform.entities.Formula;
import beaform.entities.FormulaTag;
import beaform.entities.Ingredient;

/**
 * This class bundles some debug utilities.
 *
 * @author Steven Post
 *
 */
public final class DebugUtils {

	/** a logger */
	private static final Logger LOG = LoggerFactory.getLogger(DebugUtils.class);

	/** The query to list all formulas */
	private static final String ALL_FORMULAS = "match (n:Formula) return n";

	/** Query to clear everything */
	private static final String DELETE_QUERY = "MATCH n OPTIONAL MATCH (n)-[r]-() DELETE n,r";

	private DebugUtils() {
		// Utility class.
	}

	/**
	 * List all formulas in the DB.
	 */
	public static void listAllFormulas() {
		final TransactionManager transactionMgr = GraphDbHandlerForJTA.getTransactionManager();

		try {
			transactionMgr.begin();
		}
		catch (NotSupportedException | SystemException e1) {
			LOG.error(e1.getMessage(), e1);
			return;
		}

		final EntityManager entityManager = GraphDbHandlerForJTA.getNewEntityManager();

		final Query query = entityManager.createNativeQuery(ALL_FORMULAS, Formula.class);
		@SuppressWarnings("unchecked")
		final List<Formula> formulas = query.getResultList();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Number of formulas: " + formulas.size());
		}

		for (final Formula formula : formulas) {
			LOG.debug(formula.toString());
		}

		GraphDbHandlerForJTA.tryCloseEntityManager(entityManager);

		try {
			transactionMgr.commit();
		}
		catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e1)
		{
			LOG.error(e1.getMessage(), e1);
		}

		LOG.debug(ALL_FORMULAS);
	}

	/**
	 * Delete everything in the DB.
	 */
	public static void clearDb() {
		final TransactionManager transactionMgr = GraphDbHandlerForJTA.getTransactionManager();

		try {
			transactionMgr.begin();
		}
		catch (NotSupportedException | SystemException e1) {
			LOG.error("Error starting the transaction", e1);
			return;
		}

		final EntityManager entityManager = GraphDbHandlerForJTA.getNewEntityManager();

		try {
			final Query query = entityManager.createNativeQuery(DELETE_QUERY);
			query.getSingleResult();
		}
		catch (NoResultException nre) {
			// We delete everything, there won't be a result.
			LOG.trace("No result found, which is good");
		}

		GraphDbHandlerForJTA.tryCloseEntityManager(entityManager);

		commitTransaction(transactionMgr, "Cleared DB");
	}

	/**
	 * Fills the database with some test values.
	 */
	public static void fillDb() {
		final TransactionManager transactionMgr = GraphDbHandlerForJTA.getTransactionManager();

		try {
			transactionMgr.begin();
		}
		catch (NotSupportedException | SystemException e1) {
			LOG.error("Error starting the transaction", e1);
			return;
		}

		try {
			final EntityManager entityManager = GraphDbHandlerForJTA.getNewEntityManager();

			final FormulaTag firstTag = createTag(entityManager, "First");
			final FormulaTag secondTag = createTag(entityManager, "Second");

			final Formula form1 = createFormula(entityManager, "Form1", "First test formula", firstTag, secondTag);
			final Formula form2 = createFormula(entityManager, "Form2", "Second test formula", firstTag);
			final Formula form3 = createFormula(entityManager, "Form3", "Third test formula");
			final Formula form4 = createFormula(entityManager, "Form4", "Fourth test formula");

			// Add relationships
			addIngredientsToFormula(form1, new Ingredient(form3, "50%"));
			addIngredientsToFormula(form2, new Ingredient(form4, "10%"), new Ingredient(form1, "50%"));

			GraphDbHandlerForJTA.tryCloseEntityManager(entityManager);

			commitTransaction(transactionMgr, "Stored");
		}
		catch(PersistenceException pe) {
			handlePersitenceException(transactionMgr, pe);
		}
	}

	private static void commitTransaction(final TransactionManager transactionMgr, final String logMessage) {
		try {
			transactionMgr.commit();
			LOG.info(logMessage);
		}
		catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e1)
		{
			LOG.error("Error on commit", e1);
		}
	}

	private static void handlePersitenceException(final TransactionManager transactionMgr,final PersistenceException persistenceException) {
		if (persistenceException.getCause() instanceof EntityAlreadyExistsException) {
			LOG.error("Entity already exists (executing this twice?)", persistenceException);
		}
		else {
			LOG.error("Error persisting data", persistenceException);
		}
		try {
			transactionMgr.rollback();
			LOG.error("Transaction rolled back");
		}
		catch (IllegalStateException | SecurityException | SystemException e) {
			LOG.error("Error on rollback", e);
		}
	}

	/**
	 * Create a formula
	 * @param entityManager the entity manager that is to be used
	 * @param tags any tags to be associated with the formula
	 * @return the created formula
	 */
	private static Formula createFormula(final EntityManager entityManager, final String name, final String description, final FormulaTag... tags) {
		final Formula formula = new Formula();
		formula.setName(name);
		formula.setDescription(description);
		for (final FormulaTag tag : tags) {
			formula.addTag(tag);
		}

		entityManager.persist(formula);
		return formula;
	}

	/**
	 * Create a tag.
	 * @param entityManager The entity manager to use.
	 * @param name the name for the new tag
	 * @return the new tag
	 */
	private static FormulaTag createTag(final EntityManager entityManager, final String name) {
		final FormulaTag tag = new FormulaTag();
		tag.setName(name);
		entityManager.persist(tag);

		return tag;
	}

	/**
	 * Add ingredients to a formula.
	 *
	 * @param formula The formula to add the ingredients to
	 * @param ingredients The ingredients to add.
	 */
	private static void addIngredientsToFormula(final Formula formula, final Ingredient... ingredients){
		for (final Ingredient ingredient : ingredients) {
			formula.addIngredient(ingredient.getFormula(), ingredient.getAmount());
		}
	}

}
