package beaform;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class Formula {
	private final Type type;
	private final Label label;
	private final String name;
	private final String description;

	public Formula(String name, String description) {
		this.type = Type.FORMULA;
		this.label = this.type.getLabel();

		this.name = name;
		this.description = description;
	}

	public Label getLabel() {
		return this.label;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	/**
	 * Store the Formula in the graph database.
	 *
	 * @param graphDb a handle to the graph database.
	 * @return the newly created node
	 */
	public Node persist(GraphDatabaseService graphDb) {
		Node thisNode;

		try ( Transaction tx = graphDb.beginTx()) {
			thisNode = graphDb.createNode(this.label);
			thisNode.setProperty( "name", this.name );
			thisNode.setProperty( "description", this.description );

			tx.success();
		}

		return thisNode;
	}

}
