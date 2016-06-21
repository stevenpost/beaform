package beaform.entities;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.GraphDbHandlerForJTA;

/**
 * This class handles all DB access for tags.
 *
 * @author Steven Post
 *
 */
public final class FormulaTagDAO {

	/** logger */
	private static final Logger LOG = LoggerFactory.getLogger(FormulaTagDAO.class);

	private FormulaTagDAO() {
		// Utility classes should not have a public constructor.
	}

	/**
	 * Find a tag by its name.
	 * This method assumes we are already in a transaction.
	 *
	 * @param name the name of the tag
	 * @return the tag that was found, null if no tag was found
	 */
	public static FormulaTag findByName(final String name) {

		final EntityManager entityManager = GraphDbHandlerForJTA.getNewEntityManager();

		final FormulaTag result = findByName(name, entityManager);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Found: " + result);
		}

		GraphDbHandlerForJTA.tryCloseEntityManager(entityManager);

		return result;
	}

	/**
	 * Find a tag in the DB by its name.
	 * @param name the name of the tag to look for
	 * @param entityManager the entity manager
	 * @return the tag found
	 */
	private static FormulaTag findByName(final String name, final EntityManager entityManager) {
		final String queryString = "match (n:FormulaTag { name:{name} }) return n";
		final Query query = entityManager.createNativeQuery(queryString, FormulaTag.class);
		query.setParameter("name", name);
		return (FormulaTag) query.getSingleResult();
	}

	/**
	 * Find a tag by using a tag object.
	 * This method assumes we are already in a transaction.
	 *
	 * @param tag The tag to find
	 * @return the tag that was found, null if no tag was found
	 */
	public static FormulaTag findByObject(final FormulaTag tag) {

		final EntityManager entityManager = GraphDbHandlerForJTA.getNewEntityManager();

		final FormulaTag result = findByName(tag.getName(), entityManager);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Found: " + result);
		}

		GraphDbHandlerForJTA.tryCloseEntityManager(entityManager);

		return result;
	}

}
