package beaform.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a shutdown hook to make sure the embedded DB is stopped.
 *
 * @author Steven Post
 *
 */
final class ShutDownHook extends Thread {

	/** A logger */
	private static final Logger LOG = LoggerFactory.getLogger(ShutDownHook.class);

	/** The entity manager factory */
	private final EntityManagerFactory entityManagerFact;

	/** The entity manager */
	private final EntityManager entityManager;

	/**
	 * Constructor.
	 * @param entityManager The entity manager.
	 * @param entityManagerFact The entity manager factory.
	 */
	public ShutDownHook(final EntityManager entityManager, final EntityManagerFactory entityManagerFact) {
		super();
		this.entityManager = entityManager;
		this.entityManagerFact = entityManagerFact;
	}

	/**
	 * Invoked when the hook executes.
	 */
	@Override
	public void run() {
		LOG.info("Start DB shutdown");
		if (this.entityManager.isOpen()) {
			this.entityManager.close();
		}
		if (this.entityManagerFact.isOpen()) {
			this.entityManagerFact.close();
		}
		LOG.info("DB shutdown complete");
	}
}
