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

	private final ShutDownHook shutdownHook;
	private final EntityManagerFactory emf;
	private final TransactionManager tm;

	public static GraphDbHandlerForJTA getInstance() {
		return INSTANCE;
	}

	private GraphDbHandlerForJTA() {
		//build the EntityManagerFactory as you would build in in Hibernate ORM
		this.emf = Persistence.createEntityManagerFactory("ogm-jpa-tutorial");

		//accessing JBoss's Transaction can be done differently but this one works nicely
		this.tm = extractJBossTransactionManager(this.emf);

		this.shutdownHook = new ShutDownHook(this.emf);
		Runtime.getRuntime().addShutdownHook(this.shutdownHook);
	}

	public TransactionManager getTransactionManager() {
		return this.tm;
	}

	public EntityManager getNewEntityManager() {
		return this.emf.createEntityManager();
	}

	private static TransactionManager extractJBossTransactionManager(EntityManagerFactory factory) {
		SessionFactoryImplementor sessionFactory =
						(SessionFactoryImplementor) ( (HibernateEntityManagerFactory) factory ).getSessionFactory();
		return sessionFactory.getServiceRegistry().getService( JtaPlatform.class ).retrieveTransactionManager();
	}

	public static <T> Future<T> addTask(Callable<T> task) {
		return EXEC_SERVICE.submit(task);
	}

	public static Future<?> addTask(Runnable task) {
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

		public ShutDownHook(EntityManagerFactory emf) {
			this.emf = emf;
		}

		@Override
		public void run() {
			LOG.info("Start DB shutdown");
			this.emf.close();
			LOG.info("DB shutdown complete");
		}
	}
}
