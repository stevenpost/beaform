package beaform.dao;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

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

	/** The DB service */
	private final GraphDatabaseService graphDb;

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
			public void run()
			{
				graphDb.shutdown();
			}
		} );
	}

	public GraphDatabaseService getService() {
		return this.graphDb;
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

}
