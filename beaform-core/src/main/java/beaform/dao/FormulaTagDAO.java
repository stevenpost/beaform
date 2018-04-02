package beaform.dao;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

import beaform.entities.FormulaTag;
import beaform.entities.InvalidFormulaException;

/**
 * This class handles all DB access for tags.
 *
 * @author Steven Post
 *
 */
public final class FormulaTagDAO {

	public static final String NAME = "name";

	private static final Label LABEL = Label.label("FormulaTag");
	private static final GraphDatabaseService GRAPHDB = GraphDbHandler.getDbService();

	private FormulaTagDAO() {
		// Utility classes should not have a public constructor.
	}

	public static List<FormulaTag> listAllTags() {
		final List<FormulaTag> tags;

		try ( Transaction tx = GRAPHDB.beginTx() ) {
			try ( ResourceIterator<Node> tagNodes = GRAPHDB.findNodes(LABEL)) {
				tags = listFromResourceIterator(tagNodes);
			}
			tx.success();
		}

		return tags;
	}

	private static List<FormulaTag> listFromResourceIterator(ResourceIterator<Node> tagNodes) {
		final List<FormulaTag> tags = new ArrayList<>();

		while ( tagNodes.hasNext()) {
			final Node formulaNode = tagNodes.next();
			final FormulaTag tag = nodeToTag(formulaNode);
			tags.add(tag);
		}

		return tags;
	}

	public static Node findOrCreate(String tag) {
		Node tagNode;
		try (Transaction tx = GRAPHDB.beginTx()) {

			// See if the tag exist in the DB, if so, use it, otherwise create it
			tagNode = GRAPHDB.findNode(LABEL, NAME, tag);
			if (tagNode == null) {
				tagNode = GRAPHDB.createNode(LABEL);
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
		Node tagNode;
		try (Transaction tx = GRAPHDB.beginTx()) {
			tagNode = GRAPHDB.findNode(LABEL, NAME, name);
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
