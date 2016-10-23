package beaform.dao;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import beaform.entities.FormulaTag;

/**
 * This class handles all DB access for tags.
 *
 * @author Steven Post
 *
 */
public final class FormulaTagDAO {

	public static final String NAME = "name";

	private static final Label LABEL = Label.label("FormulaTag");

	private FormulaTagDAO() {
		// Utility classes should not have a public constructor.
	}

	public static Node findOrCreate(String tag) {
		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();
		Node tagNode;
		try (Transaction tx = graphDb.beginTx()) {

			// See if the tag exist in the DB, if so, use it, otherwise create it
			tagNode = graphDb.findNode(LABEL, NAME, tag);
			if (tagNode == null) {
				tagNode = graphDb.createNode(LABEL);
				tagNode.setProperty(NAME, tag);
			}
			tx.success();
		}
		return tagNode;
	}

	public static Node findOrCreate(FormulaTag tag) {
		return findOrCreate(tag.getName());
	}

	public static Node findByName(String name) {
		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();
		Node tagNode;
		try (Transaction tx = graphDb.beginTx()) {
			tagNode = graphDb.findNode(LABEL, NAME, name);
			tx.success();
		}
		return tagNode;
	}

	public static FormulaTag nodeToTag(Node tag) {

		if (!tagNodeHasCorrectLabel(tag)) {
			throw new InvalidFormulaException();
		}

		String name = (String) tag.getProperty(NAME);

		return new FormulaTag(name);
	}

	private static boolean tagNodeHasCorrectLabel(Node tagNode) {
		final Iterable<Label> labelIt = tagNode.getLabels();
		for (Label label : labelIt) {
			if (label.name().equals(LABEL.name())) {
				return true;
			}
		}
		return false;
	}

}
