package beaform.dao;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.ConstraintType;
import org.neo4j.graphdb.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A handler for the graph database using the Neo4j Java Driver.
 *
 * @author Steven Post
 *
 */
public final class GraphDbHandler {
	private static final Logger LOG = LoggerFactory.getLogger(GraphDbHandler.class);

	private static GraphDbHandler instance;
	private static final Object INSTANCELOCK = new Object();
	private final GraphDatabaseService graphDb;

	private GraphDbHandler(final String dbPath) {
		this.graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(dbPath));
		registerShutdownHook(this.graphDb);
		enforceSchema(this.graphDb);
	}

	private void enforceSchema(GraphDatabaseService graphdb) {
		try(Transaction tx = graphdb.beginTx()) {
			Schema schema = graphdb.schema();
			Label formulaLabel = Label.label("Formula");
			addUniqueContraintIfNotExists(schema, formulaLabel, "name");
			tx.success();
			LOG.debug("Added contraint");
		}
	}

	private static void addUniqueContraintIfNotExists(Schema schema, Label formulaLabel, String property) {
		if (!hasUniqueConstraint(schema, formulaLabel, property)) {
			addUniqueConstraint(schema, formulaLabel, property);
		}
	}

	private static void addUniqueConstraint(Schema schema, Label formulaLabel, String property) {
		schema.constraintFor(formulaLabel)
		.assertPropertyIsUnique(property)
		.create();
	}

	private static boolean hasUniqueConstraint(Schema schema, Label label, String property) {
		for (ConstraintDefinition cd : schema.getConstraints(label)) {
			ConstraintType type = cd.getConstraintType();
			if (type == ConstraintType.UNIQUENESS) {
				if(hasProperty(property, cd)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean hasProperty(String property, ConstraintDefinition cd) {
		for (String field : cd.getPropertyKeys()) {
			if (property.equals(field)) {
				return true;
			}
		}
		return false;
	}

	public static GraphDatabaseService getDbService() {
		return getInstance().getService();
	}

	public static void initInstanceWithDbPath(final String dbPath) {
		synchronized (INSTANCELOCK) {
			if (instance == null) {
				instance = new GraphDbHandler(dbPath);
			}
		}
	}

	private GraphDatabaseService getService() {
		return this.graphDb;
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

	public static GraphDbHandler getInstance() {
		synchronized (INSTANCELOCK) {
			if (instance == null) {
				throw new IllegalStateException("The instance is not yet initialised");
			}
			return instance;
		}
	}

}
