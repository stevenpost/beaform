package beaform;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

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
			if (this.clearDb) {
				clearDB();
			}
			this.graphDb.shutdown();
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
