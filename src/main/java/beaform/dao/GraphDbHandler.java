package beaform.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * A handler for the graph database using JTA.
 *
 * @author Steven Post
 *
 */
public final class GraphDbHandler {

	/** The instance of this singleton */
	private static GraphDbHandler instance;

	/** A lock for accessing the instance */
	private static final Object INSTANCELOCK = new Object();

	/** The global {@link EntityManager} */
	private final EntityManager entityManager;

	/** The global EntityManager factory */
	private final EntityManagerFactory entityManagerFact;

	private GraphDbHandler(final String persistenceUnit) {
		this.entityManagerFact = Persistence.createEntityManagerFactory(persistenceUnit);

		this.entityManager = this.entityManagerFact.createEntityManager();

		final ShutDownHook shutdownHook = new ShutDownHook(this.entityManager, this.entityManagerFact);
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}

	/**
	 * Get the instance of this handler.
	 * @param persistenceUnit the persistence unit to use
	 */
	public static void initInstance(final String persistenceUnit) {
		synchronized (INSTANCELOCK) {
			if (instance == null) {
				instance = new GraphDbHandler(persistenceUnit);
			}
		}
	}

	/**
	 * Get the instance of this handler.
	 * @return the instance
	 */
	public static GraphDbHandler getInstance() {
		synchronized (INSTANCELOCK) {
			if (instance == null) {
				throw new IllegalStateException("The instance is not yet initialised");
			}
			return instance;
		}
	}

	/**
	 * Get the instance of this handler.
	 */
	public static void clearInstance() {
		synchronized (INSTANCELOCK) {
			if (instance == null) {
				throw new IllegalStateException("The instance is not yet initialised");
			}
			instance.entityManager.close();
			instance.entityManagerFact.close();
			instance = null;
		}
	}

	/**
	 * Getter for the entity manager.
	 *
	 * @return the entity manager
	 */
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	/**
	 * Try to close the entity manager.
	 * This method will flush the manager first.
	 * @param entityManager the manager to close.
	 */
	public static void tryCloseEntityManager(final EntityManager entityManager) {
		entityManager.flush();
		entityManager.close();
	}
}
