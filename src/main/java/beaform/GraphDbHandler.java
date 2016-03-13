package beaform;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphDbHandler {

	private final static GraphDbHandler INSTANCE = new GraphDbHandler();

	private GraphDatabaseService graphDb;
	private ShutDownHook shutdownHook;

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

	/**
	 * List all formulas in the DB
	 *
	 * Useful for debugging.
	 */
	public void listAllFormulas() {

		String query = "match (n:Formula) return n, n.name, n.description";
		String rows = "";

		try ( Transaction tx = this.graphDb.beginTx(); Result result = this.graphDb.execute(query)) {
			while (result.hasNext()){
				Map<String,Object> row = result.next();
				for ( Entry<String,Object> column : row.entrySet()) {
					rows += column.getKey() + ": " + column.getValue() + "; ";
				}
				rows += "\n";
			}
		}
		System.out.println("Rows: ");
		System.out.println(rows);
	}

	/**
	 * List all Base items in the DB
	 *
	 * Useful for debugging.
	 */
	public void listAllBases() {

		String query = "match (n:Base) return n, n.name, n.description";
		String rows = "";

		try ( Transaction tx = this.graphDb.beginTx(); Result result = this.graphDb.execute(query)) {
			while (result.hasNext()){
				Map<String,Object> row = result.next();
				for ( Entry<String,Object> column : row.entrySet()) {
					rows += column.getKey() + ": " + column.getValue() + "; ";
				}
				rows += "\n";
			}
		}
		System.out.println("Rows: ");
		System.out.println(rows);
	}

	public GraphDatabaseService getDbHandle() {
		return this.graphDb;
	}

	public void setClearDbOnExit(boolean clearDb) {
		this.shutdownHook.setClearDB(clearDb);
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
		private boolean clearDb = false;

		public ShutDownHook(GraphDatabaseService graphDb) {
			this.graphDb = graphDb;
		}

		public void setClearDB(boolean clearDb) {
			this.clearDb = clearDb;
		}

		@Override
		public void run() {
			LOG.info("Start DB shutdown");
			if (this.clearDb) {
				clearDB();
			}
			this.graphDb.shutdown();
			LOG.info("DB shutdown complete");
		}

		/**
		 * Delete everything in the DB
		 */
		private void clearDB(){

			GraphDatabaseService graphDb = GraphDbHandler.getInstance().getDbHandle();

			String query = "MATCH n OPTIONAL MATCH (n)-[r]-() DELETE n,r";

			try ( Transaction tx = graphDb.beginTx(); Result res = graphDb.execute(query) ) {
				tx.success();
			}

			query = "MATCH (n) RETURN count(*)";
			String rows = "";
			try ( Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query)) {
				while (result.hasNext()){
					Map<String,Object> row = result.next();
					for ( Entry<String,Object> column : row.entrySet()) {
						rows += column.getKey() + ": " + column.getValue() + "; ";
					}
					rows += "\n";
				}
			}
			System.out.println("Rows after deletion: ");
			System.out.println(rows);
		}
	}

}
