package beaform.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.transaction.jta.platform.spi.JtaPlatform;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.service.spi.ServiceRegistryImplementor;

/**
 * A handler for the graph database using JTA.
 *
 * @author Steven Post
 *
 */
public final class GraphDbHandlerForJTA {

	/** The instance of this singleton */
	private static GraphDbHandlerForJTA instance;

	/** A lock for accessing the instance */
	private static final Object INSTANCELOCK = new Object();

	/** The {@link EntityManagerFactory} */
	private final EntityManagerFactory entityManagerFact;

	/** The global {@link EntityManager} */
	private final EntityManager entityManager;

	/** The {@link TransactionManager} */
	private final TransactionManager transactionMgr;

	private GraphDbHandlerForJTA(final String persistenceUnit) {
		//build the EntityManagerFactory as you would build in in Hibernate ORM
		this.entityManagerFact = Persistence.createEntityManagerFactory(persistenceUnit);

		//accessing JBoss's Transaction can be done differently but this one works nicely
		final HibernateEntityManagerFactory hibernateEMF = (HibernateEntityManagerFactory) this.entityManagerFact;
		final SessionFactory hibernateSF = hibernateEMF.getSessionFactory();
		final SessionFactoryImplementor sessionFactory = (SessionFactoryImplementor) hibernateSF;
		final ServiceRegistryImplementor serviceRegistry = sessionFactory.getServiceRegistry();
		final JtaPlatform service = serviceRegistry.getService(JtaPlatform.class);

		this.transactionMgr = service.retrieveTransactionManager();

		// Initialize the main entity manager
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
				instance = new GraphDbHandlerForJTA(persistenceUnit);
			}
		}
	}

	/**
	 * Get the instance of this handler.
	 * @return the instance
	 */
	public static GraphDbHandlerForJTA getInstance() {
		synchronized (INSTANCELOCK) {
			if (instance == null) {
				throw new IllegalStateException("The instance is not yet initialised");
			}
			return instance;
		}
	}

	/**
	 * Gets the status of the global {@link TransactionManager}.
	 * @return the status of the transaction manager
	 * @throws SystemException If the transaction service fails in an unexpected way.
	 */
	public static int getTransactionManagerStatus() throws SystemException {
		return instance.transactionMgr.getStatus();
	}

	/**
	 * Gets the global {@link TransactionManager}.
	 * @return the {@link TransactionManager}
	 */
	public static TransactionManager getTransactionManager() {
		return instance.transactionMgr;
	}

	/**
	 * Gets the {@link EntityManagerFactory}.
	 * @return the factory
	 */
	public static EntityManagerFactory getEntityManagerFactory() {
		return instance.entityManagerFact;
	}

	/**
	 * This method creates a new {@link EntityManager}
	 * @return the new manager
	 */
	private EntityManager createNewEntityManager() {
		return this.entityManagerFact.createEntityManager();
	}

	/**
	 * This method creates a new {@link EntityManager}
	 * @return the new manager
	 */
	public static EntityManager getNewEntityManager() {
		return instance.createNewEntityManager();
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
