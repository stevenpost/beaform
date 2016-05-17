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

	private final EntityManagerFactory entityManagerFact;
	private final EntityManager entityManager;
	private final TransactionManager transactionMgr;
	private final SessionFactoryImplementor sessionFactory;

	public static GraphDbHandlerForJTA getInstance() {
		return INSTANCE;
	}

	private GraphDbHandlerForJTA() {
		//build the EntityManagerFactory as you would build in in Hibernate ORM
		this.entityManagerFact = Persistence.createEntityManagerFactory("ogm-jpa-tutorial");

		//accessing JBoss's Transaction can be done differently but this one works nicely
		final SessionFactoryImplementor sessionFactory =
						(SessionFactoryImplementor) ( (HibernateEntityManagerFactory) this.entityManagerFact ).getSessionFactory();
		this.sessionFactory = sessionFactory;
		this.transactionMgr = sessionFactory.getServiceRegistry().getService( JtaPlatform.class ).retrieveTransactionManager();

		// Initialize the main entity manager
		this.entityManager = this.entityManagerFact.createEntityManager();

		final ShutDownHook shutdownHook = new ShutDownHook(this.entityManager, this.entityManagerFact);
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}

	public TransactionManager getTransactionManager() {
		return this.transactionMgr;
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return this.entityManagerFact;
	}

	/**
	 * This method creates a new {@link EntityManager}
	 * @return the new manager
	 */
	public EntityManager createNewEntityManager() {
		return this.entityManagerFact.createEntityManager();
	}

	/**
	 * Getter for the entity manager.
	 *
	 * @return the entity manager
	 */
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	public SessionFactoryImplementor getSessionFactory() {
		return this.sessionFactory;
	}

	public static <T> Future<T> addTask(final Callable<T> task) {
		return EXEC_SERVICE.submit(task);
	}

	public static Future<?> addTask(final Runnable task) {
		return EXEC_SERVICE.submit(task);
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
