package beaform;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Main {
	private static GraphDatabaseService graphDb;

	public static void main(String[] args) {

		initDB();
		fillDB();
		searchDB();
		clearDB();

	}

	private static void initDB() {
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("neo4j/db"));
		Runtime.getRuntime().addShutdownHook(new ShutDownHook(graphDb));
	}

	private static void fillDB() {
		Node firstNode;
		Node secondNode;
		Node thirdNode;
		Relationship relationship;

		try ( Transaction tx = graphDb.beginTx()) {
			firstNode = graphDb.createNode();
			firstNode.setProperty( "message", "Hello, " );
			secondNode = graphDb.createNode();
			secondNode.setProperty( "message", "World!" );

			relationship = firstNode.createRelationshipTo( secondNode, RelTypes.CONTAINS );
			relationship.setProperty( "message", "brave Neo4j " );

			thirdNode = graphDb.createNode(new Label(){

				@Override
				public String name() {
					return "TestNode";
				}

			});
			thirdNode.setProperty("message", "Hello, ");

			tx.success();
		}
	}

	private static void searchDB(){

		String query = "match (n {message: 'Hello, '}) return n, n.message";
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

		Node firstNode;
		Label lbl = new Label() {

			@Override
			public String name() {
				return "TestNode";
			}
		};

		try ( Transaction tx = graphDb.beginTx()) {
			firstNode = graphDb.findNode(lbl, "message", "Hello, ");
			System.out.println(firstNode.getProperty("message"));
		}

		System.out.println("Rows: ");
		System.out.println(rows);
	}

	private static void clearDB(){
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
