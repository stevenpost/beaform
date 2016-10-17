package beaform.debug;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.collections.ListUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.FormulaDAO;
import beaform.dao.FormulaTagDAO;
import beaform.dao.GraphDbHandler;
import beaform.dao.RelTypes;
import beaform.entities.Formula;
import beaform.entities.FormulaTag;

/**
 * This class bundles some debug utilities.
 *
 * @author Steven Post
 *
 */
public final class DebugUtils {

	/** a logger */
	private static final Logger LOG = LoggerFactory.getLogger(DebugUtils.class);

	/** The query to list all formulas */
	private static final String ALL_FORMULAS = "match (n:Formula) return n";

	private static final String ALL_TAGS = "match (n:FormulaTag) return n";

	/** Query to clear everything */
	private static final String DELETE_QUERY = "MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r";

	private DebugUtils() {
		// Utility class.
	}

	/**
	 * List all formulas in the DB.
	 */
	public static void listAllFormulas() {

		final GraphDatabaseService graphDb = GraphDbHandler.getInstance().getService();

		Label label = Label.label("Formula");
		try ( Transaction tx = graphDb.beginTx() ) {
			try ( ResourceIterator<Node> formulas = graphDb.findNodes(label)) {
				ArrayList<Node> formulaNodes = new ArrayList<>();
				while ( formulas.hasNext()) {
					formulaNodes.add(formulas.next());
				}

				if (LOG.isDebugEnabled()) {
					LOG.debug("Number of formulas: " + formulaNodes.size());
				}

				for (Node node : formulaNodes) {
					Formula form = FormulaDAO.nodeToFormula(node);
					LOG.info(form.toString());
				}
			}
			tx.success();
		}

		LOG.debug(ALL_FORMULAS);
	}

	/**
	 * List all formulas in the DB.
	 */
	public static void listAllTags() {

		final GraphDatabaseService graphDb = GraphDbHandler.getInstance().getService();

		Label label = Label.label("FormulaTag");
		try ( Transaction tx = graphDb.beginTx() ) {
			try ( ResourceIterator<Node> formulas = graphDb.findNodes(label)) {
				ArrayList<Node> tagNodes = new ArrayList<>();
				while ( formulas.hasNext()) {
					tagNodes.add(formulas.next());
				}

				if (LOG.isDebugEnabled()) {
					LOG.debug("Number of tags: " + tagNodes.size());
				}

				for (Node node : tagNodes) {
					FormulaTag form = FormulaTagDAO.nodeToTag(node);
					LOG.info(form.toString());
				}
			}
			tx.success();
		}

		LOG.debug(ALL_TAGS);
	}

	/**
	 * Delete everything in the DB.
	 */
	public static void clearDb() {

		final GraphDatabaseService graphDb = GraphDbHandler.getInstance().getService();
		try ( Transaction tx = graphDb.beginTx() ) {
			graphDb.execute(DELETE_QUERY);
			tx.success();
		}

		LOG.info("DB cleared");

	}

	/**
	 * Fills the database with some test values.
	 */
	public static void fillDb() {
		final GraphDatabaseService graphDb = GraphDbHandler.getInstance().getService();

		try ( Transaction tx = graphDb.beginTx() ) {

			final FormulaTag firstTag = FormulaTagDAO.nodeToTag(FormulaTagDAO.findOrCreate("First"));
			final FormulaTag secondTag = FormulaTagDAO.nodeToTag(FormulaTagDAO.findOrCreate("Second"));

			final FormulaTag[] form1Tags = new FormulaTag[]{firstTag, secondTag};
			final Node form1 = createFormula("Form1", "First test formula", form1Tags);
			final FormulaTag[] form2Tags = new FormulaTag[]{firstTag};
			final Node form2 = createFormula("Form2", "Second test formula", form2Tags);
			final Node form3 = createFormula("Form3", "Third test formula");
			final Node form4 = createFormula("Form4", "Fourth test formula");

			// Add relationships
			Relationship rel1 = form1.createRelationshipTo(form3, RelTypes.HASINGREDIENT);
			rel1.setProperty("amount", "50%");

			Relationship rel2 = form2.createRelationshipTo(form4, RelTypes.HASINGREDIENT);
			rel2.setProperty("amount", "10%");
			Relationship rel3 = form2.createRelationshipTo(form1, RelTypes.HASINGREDIENT);
			rel3.setProperty("amount", "50%");

			tx.success();
		}
	}

	/**
	 * Create a formula
	 * @param entityManager the entity manager that is to be used
	 * @return the created formula
	 */
	private static Node createFormula(final String name,
	                                  final String description) {
		return FormulaDAO.addFormula(name, description, "", ListUtils.EMPTY_LIST, ListUtils.EMPTY_LIST);
	}

	/**
	 * Create a formula
	 * @param entityManager the entity manager that is to be used
	 * @param tags any tags to be associated with the formula
	 * @return the created formula
	 */
	private static Node createFormula(final String name,
	                                  final String description,
	                                  final FormulaTag[] tags) {

		return FormulaDAO.addFormula(name, description, "", ListUtils.EMPTY_LIST, Arrays.asList(tags));
	}

}
