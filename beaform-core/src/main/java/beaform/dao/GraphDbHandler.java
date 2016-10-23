package beaform.dao;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * A handler for the graph database using the Neo4j Java Driver.
 *
 * @author Steven Post
 *
 */
public final class GraphDbHandler {

	private static GraphDbHandler instance;
	private static final Object INSTANCELOCK = new Object();
	private final GraphDatabaseService graphDb;

	public static GraphDatabaseService getDbService() {
		return getInstance().getService();
	}

	/**
	 * Initialize the handler.
	 * @param dbPath the path to the DB (may be relative)
	 */
	public static void initInstance(final String dbPath) {
		synchronized (INSTANCELOCK) {
			if (instance == null) {
				instance = new GraphDbHandler(dbPath);
			}
		}
	}

	private GraphDbHandler(final String dbPath) {
		this.graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(dbPath));
		registerShutdownHook(this.graphDb);
	}

	private static void registerShutdownHook( final GraphDatabaseService graphDb ) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running application).
		Runtime.getRuntime().addShutdownHook( new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		} );
	}

	private GraphDatabaseService getService() {
		return this.graphDb;
	}

	public static GraphDbHandler getInstance() {
		synchronized (INSTANCELOCK) {
			if (instance == null) {
				throw new IllegalStateException("The instance is not yet initialised");
			}
			return instance;
		}
	}

}
