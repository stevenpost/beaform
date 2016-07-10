package beaform.debug;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.GraphDbHandler;
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

		final EntityManager entityManager = GraphDbHandler.getInstance().getEntityManager();
		entityManager.getTransaction().begin();

		final Query query = entityManager.createNativeQuery(ALL_FORMULAS, Formula.class);
		final List<Formula> formulas = query.getResultList();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Number of formulas: " + formulas.size());
		}

		for (final Formula formula : formulas) {
			LOG.debug(formula.toString());
		}

		entityManager.getTransaction().commit();

		LOG.debug(ALL_FORMULAS);
	}

	/**
	 * Delete everything in the DB.
	 */
	public static void clearDb() {
		final EntityManager entityManager = GraphDbHandler.getInstance().getEntityManager();
		entityManager.getTransaction().begin();

		try {
			final Query query = entityManager.createNativeQuery(DELETE_QUERY);
			query.getSingleResult();
		}
		catch (NoResultException nre) {
			// We delete everything, there won't be a result.
			LOG.trace("No result found, which is good", nre);
		}

		entityManager.getTransaction().commit();
		LOG.info("DB cleared");

	}

	/**
	 * Fills the database with some test values.
	 */
	public static void fillDb() {
		final EntityManager entityManager = GraphDbHandler.getInstance().getEntityManager();
		entityManager.getTransaction().begin();

		try {

			final FormulaTag firstTag = createTag(entityManager, "First");
			final FormulaTag secondTag = createTag(entityManager, "Second");

			final FormulaTag[] form1Tags = new FormulaTag[]{firstTag, secondTag};
			final Formula form1 = createFormula(entityManager, "Form1", "First test formula", form1Tags);
			final FormulaTag[] form2Tags = new FormulaTag[]{firstTag};
			final Formula form2 = createFormula(entityManager, "Form2", "Second test formula", form2Tags);
			final Formula form3 = createFormula(entityManager, "Form3", "Third test formula");
			final Formula form4 = createFormula(entityManager, "Form4", "Fourth test formula");

			// Add relationships
			addIngredientsToFormula(form1, new Ingredient[]{new Ingredient(form3, "50%")});
			final Ingredient[] form2Ingredients = new Ingredient[]{
			                                                       new Ingredient(form4, "10%"),
			                                                       new Ingredient(form1, "50%")};
			addIngredientsToFormula(form2, form2Ingredients);

			entityManager.getTransaction().commit();
			entityManager.detach(form1);
			entityManager.detach(form2);
			entityManager.detach(form3);
			entityManager.detach(form4);
			entityManager.detach(firstTag);
			entityManager.detach(secondTag);
		}
		catch (PersistenceException pe) {
			entityManager.getTransaction().rollback();
			throw pe;
		}
	}

	/**
	 * Create a formula
	 * @param entityManager the entity manager that is to be used
	 * @return the created formula
	 */
	private static Formula createFormula(final EntityManager entityManager,
	                                     final String name,
	                                     final String description) {
		return createFormula(entityManager, name, description, null);
	}

	/**
	 * Create a formula
	 * @param entityManager the entity manager that is to be used
	 * @param tags any tags to be associated with the formula
	 * @return the created formula
	 */
	private static Formula createFormula(final EntityManager entityManager,
	                                     final String name,
	                                     final String description,
	                                     final FormulaTag[] tags) {

		final Formula formula = new Formula(name, description, "0%");
		if (tags != null) {
			for (final FormulaTag tag : tags) {
				formula.addTag(tag);
			}
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
		final FormulaTag tag = new FormulaTag(name);
		entityManager.persist(tag);

		return tag;
	}

	/**
	 * Add ingredients to a formula.
	 *
	 * @param formula The formula to add the ingredients to
	 * @param ingredients The ingredients to add.
	 */
	private static void addIngredientsToFormula(final Formula formula, final Ingredient[] ingredients){
		for (final Ingredient ingredient : ingredients) {
			formula.addIngredient(ingredient.getFormula(), ingredient.getAmount());
		}
	}

}
