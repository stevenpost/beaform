package beaform.debug;

import java.util.ArrayList;

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

	private static final Logger LOG = LoggerFactory.getLogger(DebugUtils.class);

	private static final String LIST_ALL_FORMULAS_QRY = "match (n:Formula) return n";
	private static final String LIST_ALL_TAGS_QRY = "match (n:FormulaTag) return n";
	private static final String CLEAR_DB_QUERY = "MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r";
	private static final String RELATION_AMOUNT = "amount";

	private DebugUtils() {
		// Utility class.
	}

	public static void listAllFormulas() {

		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();

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

		LOG.debug(LIST_ALL_FORMULAS_QRY);
	}

	public static void listAllTags() {

		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();

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

		LOG.debug(LIST_ALL_TAGS_QRY);
	}

	public static void clearDb() {

		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();
		try ( Transaction tx = graphDb.beginTx() ) {
			graphDb.execute(CLEAR_DB_QUERY);
			tx.success();
		}

		LOG.info("DB cleared");

	}

	public static void fillDb() {
		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();

		try ( Transaction tx = graphDb.beginTx() ) {

			final FormulaTag firstTag = FormulaTagDAO.nodeToTag(FormulaTagDAO.findOrCreate("First"));
			final FormulaTag secondTag = FormulaTagDAO.nodeToTag(FormulaTagDAO.findOrCreate("Second"));

			final FormulaTag[] form1Tags = new FormulaTag[]{firstTag, secondTag};
			final Node form1 = createFormula("Form1", "First test formula", "5g", form1Tags);
			final FormulaTag[] form2Tags = new FormulaTag[]{firstTag};
			final Node form2 = createFormula("Form2", "Second test formula", "10g", form2Tags);
			final Node form3 = createFormula("Form3", "Third test formula", "1g");
			final Node form4 = createFormula("Form4", "Fourth test formula", "2g");

			// Add relationships
			Relationship rel1 = form1.createRelationshipTo(form3, RelTypes.HASINGREDIENT);
			rel1.setProperty(RELATION_AMOUNT, "50%");

			Relationship rel2 = form2.createRelationshipTo(form4, RelTypes.HASINGREDIENT);
			rel2.setProperty(RELATION_AMOUNT, "10%");
			Relationship rel3 = form2.createRelationshipTo(form1, RelTypes.HASINGREDIENT);
			rel3.setProperty(RELATION_AMOUNT, "50%");

			tx.success();
		}
	}

	/**
	 * Create a formula
	 * @param entityManager the entity manager that is to be used
	 * @return the created formula
	 */
	private static Node createFormula(final String name,
	                                  final String description,
	                                  final String totalAmount) {
		Formula formula = new Formula(name, description, totalAmount);
		return FormulaDAO.addFormula(formula);
	}

	/**
	 * Create a formula
	 * @param entityManager the entity manager that is to be used
	 * @param tags any tags to be associated with the formula
	 * @return the created formula
	 */
	private static Node createFormula(final String name,
	                                  final String description,
	                                  final String totalAmount,
	                                  final FormulaTag[] tags) {
		Formula formula = new Formula(name, description, totalAmount);
		for (FormulaTag tag : tags) {
			formula.addTag(tag);
		}
		return FormulaDAO.addFormula(formula);
	}

}
