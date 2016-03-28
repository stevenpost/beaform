package beaform;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphDbHandler {

	private final static GraphDbHandler INSTANCE = new GraphDbHandler();

	private GraphDatabaseService graphDb;
	private ShutDownHook shutdownHook;
	private final ExecutorService execService = Executors.newSingleThreadExecutor();

	public static GraphDbHandler getInstance() {
		return INSTANCE;
	}

	private GraphDbHandler() {
		initDB();
	}

	/**
	 * Initialize the embedded DB
	 */
	private void initDB() {
		this.graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("neo4j/db"));
		this.shutdownHook = new ShutDownHook(this.graphDb);
		Runtime.getRuntime().addShutdownHook(this.shutdownHook);
	}

	public GraphDatabaseService getDbHandle() {
		return this.graphDb;
	}

	public <T> Future<T> addTask(Callable<T> task) {
		return this.execService.submit(task);
	}

	public Future<?> addTask(Runnable task) {
		return this.execService.submit(task);
	}

	/**
	 * This class is a shutdownhook to make sure the embedded DB is stopped.
	 *
	 * @author steven
	 *
	 */
	private final static class ShutDownHook extends Thread {

		private static final Logger LOG = LoggerFactory.getLogger(ShutDownHook.class);

		private final GraphDatabaseService graphDb;

		public ShutDownHook(GraphDatabaseService graphDb) {
			this.graphDb = graphDb;
		}

		@Override
		public void run() {
			LOG.info("Start DB shutdown");
			this.graphDb.shutdown();
			LOG.info("DB shutdown complete");
		}
	}

}
