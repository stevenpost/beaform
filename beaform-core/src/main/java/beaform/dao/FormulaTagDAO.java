package beaform.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.entities.FormulaTag;

/**
 * This class handles all DB access for tags.
 *
 * @author Steven Post
 *
 */
public final class FormulaTagDAO {

	/** logger */
	private static final Logger LOG = LoggerFactory.getLogger(FormulaTagDAO.class);

	/** The query to search a tag by name */
	private static final String QRY_TAG_BY_NAME = "match (n:FormulaTag { name:{name} }) return n";

	private FormulaTagDAO() {
		// Utility classes should not have a public constructor.
	}

	/**
	 * Find a tag in the DB by its name.
	 * @param name the name of the tag to look for
	 * @param entityManager the entity manager
	 * @return the tag found
	 */
	private static FormulaTag findByName(final String name, final EntityManager entityManager) {
		final Query query = entityManager.createNativeQuery(QRY_TAG_BY_NAME, FormulaTag.class);
		query.setParameter("name", name);
		return (FormulaTag) query.getSingleResult();
	}

	/**
	 * Find a tag by using a tag object.
	 * This method assumes we are already in a transaction.
	 *
	 * @param tag The tag to find
	 * @return the tag that was found
	 */
	public static FormulaTag findByObject(final FormulaTag tag) {

		final EntityManager entityManager = GraphDbHandler.getInstance().getEntityManager();

		final FormulaTag result = findByName(tag.getName(), entityManager);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Found: " + result);
		}

		return result;
	}

}
