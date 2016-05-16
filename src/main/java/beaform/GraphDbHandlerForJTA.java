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

public class GraphDbHandlerForJTA {

	private static final GraphDbHandlerForJTA INSTANCE = new GraphDbHandlerForJTA();
	private static final ExecutorService EXEC_SERVICE = Executors.newSingleThreadExecutor();

	private final EntityManagerFactory emf;
	private final EntityManager em;
	private final TransactionManager tm;
	private final SessionFactoryImplementor sessionFactory;

	public static GraphDbHandlerForJTA getInstance() {
		return INSTANCE;
	}

	private GraphDbHandlerForJTA() {
		//build the EntityManagerFactory as you would build in in Hibernate ORM
		this.emf = Persistence.createEntityManagerFactory("ogm-jpa-tutorial");

		//accessing JBoss's Transaction can be done differently but this one works nicely
		final SessionFactoryImplementor sessionFactory =
						(SessionFactoryImplementor) ( (HibernateEntityManagerFactory) this.emf ).getSessionFactory();
		this.sessionFactory = sessionFactory;
		this.tm = sessionFactory.getServiceRegistry().getService( JtaPlatform.class ).retrieveTransactionManager();

		// Initialize the main entity manager
		this.em = this.emf.createEntityManager();

		final ShutDownHook shutdownHook = new ShutDownHook(this.em, this.emf);
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}

	public TransactionManager getTransactionManager() {
		return this.tm;
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return this.emf;
	}

	public EntityManager getEntityManager() {
		return this.em;
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
	 * @author steven
	 *
	 */
	private final static class ShutDownHook extends Thread {

		private static final Logger LOG = LoggerFactory.getLogger(ShutDownHook.class);

		private final EntityManagerFactory emf;
		private final EntityManager em;

		public ShutDownHook(final EntityManager em, final EntityManagerFactory emf) {
			super();
			this.em = em;
			this.emf = emf;
		}

		@Override
		public void run() {
			LOG.info("Start DB shutdown");
			this.em.close();
			this.emf.close();
			LOG.info("DB shutdown complete");
		}
	}
}
