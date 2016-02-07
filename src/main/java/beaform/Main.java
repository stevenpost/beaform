package beaform;

import java.io.File;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Main {
	private static GraphDatabaseService graphDb;

	public static void main(String[] args) {

		initDB();

		Node firstNode;
		Node secondNode;
		Relationship relationship;



		try ( Transaction tx = graphDb.beginTx()) {
			firstNode = graphDb.createNode();
			firstNode.setProperty( "message", "Hello, " );
			secondNode = graphDb.createNode();
			secondNode.setProperty( "message", "World!" );

			relationship = firstNode.createRelationshipTo( secondNode, RelTypes.CONTAINS );
			relationship.setProperty( "message", "brave Neo4j " );

			tx.success();
		}

		try ( Transaction tx = graphDb.beginTx()) {
			System.out.print( firstNode.getProperty( "message" ) );
			System.out.print( relationship.getProperty( "message" ) );
			System.out.print( secondNode.getProperty( "message" ) );
		}

		try ( Transaction tx = graphDb.beginTx()) {
			// let's remove the data
			firstNode.getSingleRelationship( RelTypes.CONTAINS, Direction.OUTGOING ).delete();
			firstNode.delete();
			secondNode.delete();
			tx.success();
		}

	}

	private static void initDB() {
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("neo4j/db"));
		Runtime.getRuntime().addShutdownHook(new ShutDownHook(graphDb));
	}


	private static class ShutDownHook extends Thread {

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
