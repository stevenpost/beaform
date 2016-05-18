package beaform;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.TransactionManager;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.transaction.jta.platform.spi.JtaPlatform;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A handler for the graph database using JTA.
 *
 * @author Steven Post
 *
 */
public class GraphDbHandlerForJTA {

	/** The instance of this singleton */
	private static final GraphDbHandlerForJTA INSTANCE = new GraphDbHandlerForJTA();

	/** An executor service for handling DB tasks */
	private static final ExecutorService EXEC_SERVICE = Executors.newSingleThreadExecutor();

	/** The {@link EntityManagerFactory} */
	private final transient EntityManagerFactory entityManagerFact;

	/** The global {@link EntityManager} */
	private final EntityManager entityManager;

	/** The {@link TransactionManager} */
	private final transient TransactionManager transactionMgr;

	/**
	 * Get the instance of this handler.
	 * @return the instance
	 */
	public static GraphDbHandlerForJTA getInstance() {
		return INSTANCE;
	}

	private GraphDbHandlerForJTA() {
		//build the EntityManagerFactory as you would build in in Hibernate ORM
		this.entityManagerFact = Persistence.createEntityManagerFactory("ogm-jpa-tutorial");

		//accessing JBoss's Transaction can be done differently but this one works nicely
		final SessionFactoryImplementor sessionFactory =
						(SessionFactoryImplementor) ( (HibernateEntityManagerFactory) this.entityManagerFact ).getSessionFactory();
		this.transactionMgr = sessionFactory.getServiceRegistry().getService( JtaPlatform.class ).retrieveTransactionManager();

		// Initialize the main entity manager
		this.entityManager = this.entityManagerFact.createEntityManager();

		final ShutDownHook shutdownHook = new ShutDownHook(this.entityManager, this.entityManagerFact);
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}

	/**
	 * Gets the global {@link TransactionManager}.
	 * @return the {@link TransactionManager}
	 */
	public static TransactionManager getTransactionManager() {
		return INSTANCE.transactionMgr;
	}

	/**
	 * Gets the {@link EntityManagerFactory}.
	 * @return the factory
	 */
	public static EntityManagerFactory getEntityManagerFactory() {
		return INSTANCE.entityManagerFact;
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
		return INSTANCE.createNewEntityManager();
	}

	/**
	 * Getter for the entity manager.
	 *
	 * @return the entity manager
	 */
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	public static <T> Future<T> addTask(final Callable<T> task) {
		return EXEC_SERVICE.submit(task);
	}

	public static Future<?> addTask(final Runnable task) {
		return EXEC_SERVICE.submit(task);
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

	/**
	 * This class is a shutdownhook to make sure the embedded DB is stopped.
	 *
	 * @author Steven Post
	 *
	 */
	private final static class ShutDownHook extends Thread {

		private static final Logger LOG = LoggerFactory.getLogger(ShutDownHook.class);

		private final EntityManagerFactory entityManagerFact;
		private final EntityManager entityManager;

		public ShutDownHook(final EntityManager entityManager, final EntityManagerFactory entityManagerFact) {
			super();
			this.entityManager = entityManager;
			this.entityManagerFact = entityManagerFact;
		}

		@Override
		public void run() {
			LOG.info("Start DB shutdown");
			this.entityManager.close();
			this.entityManagerFact.close();
			LOG.info("DB shutdown complete");
		}
	}
}
