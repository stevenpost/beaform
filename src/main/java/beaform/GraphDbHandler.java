package beaform;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class GraphDbHandler {

	private final static GraphDbHandler INSTANCE = new GraphDbHandler();

	private GraphDatabaseService graphDb;

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
		Runtime.getRuntime().addShutdownHook(new ShutDownHook(this.graphDb));
	}

	public GraphDatabaseService getDbHandle() {
		return this.graphDb;
	}

	/**
	 * This class is a shutdownhook to make sure the embedded DB is stopped.
	 *
	 * @author steven
	 *
	 */
	private final static class ShutDownHook extends Thread {

		private final GraphDatabaseService graphDb;

		public ShutDownHook(GraphDatabaseService graphDb) {
			this.graphDb = graphDb;
		}

		@Override
		public void run() {
			this.graphDb.shutdown();
		}
	}

}
