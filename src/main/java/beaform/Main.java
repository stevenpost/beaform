package beaform;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.graphdb.GraphDatabaseService;
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

		Base base1 = new Base("Base123", "First test base");
		Node firstBase = base1.persist(graphDb);
		Base base2 = new Base("Base456", "Second test base");
		Node secondBase = base2.persist(graphDb);

		Formula form1 = new Formula("Form1", "First test formula");
		Node firstFormula = form1.persist(graphDb);
		Formula form2 = new Formula("Form2", "Second test formula");
		Node secondFormula = form2.persist(graphDb);

		// Add relationships
		try ( Transaction tx = graphDb.beginTx()) {
			Relationship relationship;

			relationship = firstFormula.createRelationshipTo( secondBase, RelTypes.CONTAINS );
			relationship.setProperty( "amount", "10%" );

			relationship = secondFormula.createRelationshipTo( firstBase, RelTypes.CONTAINS );
			relationship.setProperty( "amount", "50%" );

			relationship = secondFormula.createRelationshipTo( firstFormula, RelTypes.CONTAINS );
			relationship.setProperty( "amount", "50%" );

			tx.success();
		}
	}

	private static void searchDB(){

		String query = "match (n:Formula) return n, n.name, n.description";
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
