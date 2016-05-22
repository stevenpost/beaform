package beaform.entities;

import javax.persistence.EntityManager;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.GraphDbHandlerForJTA;

/**
 * This class handles all DB access for tags.
 *
 * @author Steven Post
 *
 */
public class FormulaTagDAO {

	/** logger */
	private static final Logger LOG = LoggerFactory.getLogger(FormulaTagDAO.class);

	/**
	 * Find a tag by its name.
	 * This method assumes we are already in a transaction.
	 *
	 * @param name the name of the tag
	 * @return the tag that was found, null if no tag was found
	 * @throws NotSupportedException If the calling thread is already
	 *         associated with a transaction,
	 *         and nested transactions are not supported.
	 * @throws SystemException If the transaction service fails in an unexpected way.
	 */
	public FormulaTag findByName(final String name) throws NotSupportedException, SystemException {

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
	private FormulaTag findByName(final String name, final EntityManager entityManager) {
		final String query = "match (n:FormulaTag { name:'" + name + "' }) return n";
		return (FormulaTag) entityManager.createNativeQuery(query, FormulaTag.class).getSingleResult();
	}

	/**
	 * Find a tag by using a tag object.
	 * This method assumes we are already in a transaction.
	 *
	 * @param tag The tag to find
	 * @return the tag that was found, null if no tag was found
	 * @throws NotSupportedException If the calling thread is already
	 *         associated with a transaction,
	 *         and nested transactions are not supported.
	 * @throws SystemException If the transaction service fails in an unexpected way.
	 */
	public FormulaTag findByObject(final FormulaTag tag) throws NotSupportedException, SystemException {

		final EntityManager entityManager = GraphDbHandlerForJTA.getNewEntityManager();

		final FormulaTag result = findByName(tag.getName(), entityManager);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Found: " + result);
		}

		GraphDbHandlerForJTA.tryCloseEntityManager(entityManager);

		return result;
	}

}
