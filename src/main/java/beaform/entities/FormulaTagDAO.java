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
		FormulaTag result;

		final EntityManager entityManager = GraphDbHandlerForJTA.getNewEntityManager();

		final String query = "match (n:FormulaTag { name:'" + name + "' }) return n";
		result = (FormulaTag) entityManager.createNativeQuery(query, FormulaTag.class).getSingleResult();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Found: " + result);
		}

		entityManager.flush();
		entityManager.close();

		return result;
	}

	/**
	 * Find a tag by using a tag object.
	 * This method assumes we are already in a transaction.
	 *
	 * @param The tag to find
	 * @return the tag that was found, null if no tag was found
	 * @throws NotSupportedException If the calling thread is already
	 *         associated with a transaction,
	 *         and nested transactions are not supported.
	 * @throws SystemException If the transaction service fails in an unexpected way.
	 */
	public FormulaTag findByObject(final FormulaTag name) throws NotSupportedException, SystemException {
		FormulaTag result;

		final EntityManager entityManager = GraphDbHandlerForJTA.getNewEntityManager();

		final String query = "match (n:FormulaTag { name:'" + name.getName() + "' }) return n";
		result = (FormulaTag) entityManager.createNativeQuery(query, FormulaTag.class).getSingleResult();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Found: " + result);
		}

		entityManager.flush();
		entityManager.close();

		return result;
	}

}
