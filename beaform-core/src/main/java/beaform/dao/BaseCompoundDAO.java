package beaform.dao;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import beaform.entities.BaseCompound;

public final class BaseCompoundDAO {

	public static final String NAME = "name";
	public static final String STR_LABEL = "BaseCompound";

	private static final Label LABEL = Label.label(STR_LABEL);
	private static final GraphDatabaseService GRAPHDB = GraphDbHandler.getDbService();

	private BaseCompoundDAO() {
		// private constructor, because this is a utility class.
	}

	public static Node findOrCreate(BaseCompound bc) {
		Node bcNode;
		try (Transaction tx = GRAPHDB.beginTx()) {

			// See if the tag exist in the DB, if so, use it, otherwise create it
			bcNode = GRAPHDB.findNode(LABEL, NAME, bc.getName());
			if (bcNode == null) {
				bcNode = GRAPHDB.createNode(LABEL);
				bcNode.setProperty(NAME, bc.getName());
			}
			tx.success();
		}
		return bcNode;
	}

	public static BaseCompound findByName(final String name) {
		Node bcNode;
		try (Transaction tx = GRAPHDB.beginTx()) {
			bcNode = GRAPHDB.findNode(LABEL, NAME, name);
			tx.success();
		}
		return nodeToBaseCompound(bcNode);
	}

	public static BaseCompound nodeToBaseCompound(final Node node) {
		final String name = (String) node.getProperty(NAME);
		return new BaseCompound(name);
	}

}
