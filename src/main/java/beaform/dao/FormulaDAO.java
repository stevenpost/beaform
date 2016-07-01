package beaform.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.entities.Formula;
import beaform.entities.FormulaTag;
import beaform.entities.Ingredient;

/**
 * This class handles all DB access for formulas.
 *
 * @author Steven Post
 *
 */
public final class FormulaDAO {

	private static final Logger LOG = LoggerFactory.getLogger(FormulaDAO.class);

	/** Query to search for a formula by tag */
	private static final String FORMULA_BY_TAG = "MATCH (t:FormulaTag { name:{name} })<-[r]-(f:Formula) RETURN f";

	private FormulaDAO() {
		// private constructor, because this is a utility class.
	}

	public static List<Ingredient> getIngredients(final Formula formula) {

		final EntityManager entityManager = GraphDbHandler.getInstance().getEntityManager();
		entityManager.getTransaction().begin();
		final Formula retrievedFormula = entityManager.find(Formula.class, formula.getName());
		final List<Ingredient> retList = retrievedFormula.getIngredients();

		entityManager.getTransaction().commit();

		return retList;
	}

	public static void updateExisting(final String name,
	                                  final String description,
	                                  final String totalAmount,
	                                  final List<Ingredient> ingredients,
	                                  final List<FormulaTag> tags) {

		final EntityManager entityManager = GraphDbHandler.getInstance().getEntityManager();
		entityManager.getTransaction().begin();

		final Formula formula = entityManager.find(Formula.class, name);

		setFormulaProperties(formula, description, totalAmount);

		clearFormulaRelations(formula);

		addTags(tags, formula);
		addIngredientsToFormula(formula, ingredients);

		entityManager.getTransaction().commit();

		entityManager.detach(formula);
	}

	private static void addIngredientsToFormula(final Formula formula, final List<Ingredient> ingredients) {
		for (final Ingredient ingredient : ingredients) {
			// We should only be holding existing Formulas at this point
			formula.addIngredient(ingredient.getFormula(), ingredient.getAmount());
		}
	}

	public static void addFormula(final String name,
	                              final String description,
	                              final String totalAmount,
	                              final List<Ingredient> ingredients,
	                              final List<FormulaTag> tags) {

		final EntityManager entityManager = GraphDbHandler.getInstance().getEntityManager();
		entityManager.getTransaction().begin();

		final Formula formula = new Formula(name, description, totalAmount);

		addTags(tags, formula);

		addIngredientsToFormula(formula, ingredients);

		entityManager.persist(formula);

		entityManager.getTransaction().commit();

		entityManager.detach(formula);

	}

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
	private static void addTags(final List<FormulaTag> tags, final Formula formula) {

		for (final FormulaTag tag : tags) {
			addTagToFormula(formula, tag);
		}
	}

	private static void addTagToFormula(final Formula formula, final FormulaTag tag) {
		// See if the tag exist in the DB, if so, use it.
		FormulaTag tagToAdd;
		try {
			tagToAdd = FormulaTagDAO.findByObject(tag);
		}
		catch (NoResultException e1) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("No tag with name " + tag.getName() + " found: " + e1.getMessage(), e1);
			}
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
	 */
	public static Formula findFormulaByName(final String name) {

		final EntityManager entityManager = GraphDbHandler.getInstance().getEntityManager();
		entityManager.getTransaction().begin();

		final Formula result = entityManager.find(Formula.class, name);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Found: " + result);
		}

		entityManager.getTransaction().commit();
		entityManager.detach(result);

		return result;
	}

	public static List<Formula> findFormulasByTag(final String tagName) {

		final EntityManager entityManager = GraphDbHandler.getInstance().getEntityManager();
		entityManager.getTransaction().begin();

		final List<Formula> result = findByTag(tagName, entityManager);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Found: " + result);
		}

		entityManager.getTransaction().commit();

		for (final Formula formula : result) {
			entityManager.detach(formula);
		}

		return result;
	}

	private static List<Formula> findByTag(final String tagName, final EntityManager entityManager) {
		final Query query = entityManager.createNativeQuery(FORMULA_BY_TAG, Formula.class);
		query.setParameter("name", tagName);
		return query.getResultList();
	}

	private static void clearFormulaRelations(final Formula formula) {
		formula.deleteAllIngredients();
		formula.deleteAllTags();
	}

}
