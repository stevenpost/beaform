package beaform.debug;

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.Schema;
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

	private static final String CLEAR_DB_QUERY = "MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r";
	private static final String RELATION_AMOUNT = "amount";

	private DebugUtils() {
		// Utility class.
	}

	public static void listAllFormulas() {
		List<Formula> formulas = FormulaDAO.listAllFormulas();
		LOG.info("Listing " + formulas.size() + " formulas");
		for (Formula formula : formulas) {
			LOG.info(formula.toString());
		}
	}

	public static void listAllTags() {
		List<FormulaTag> tags = FormulaTagDAO.listAllTags();
		for (FormulaTag tag : tags) {
			LOG.info(tag.toString());
		}
	}

	public static void clearDb() {
		deleteAllNodes();
		clearConstraints();
		LOG.info("DB cleared");
	}

	private static void deleteAllNodes() {
		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();
		try ( Transaction tx = graphDb.beginTx() ) {
			graphDb.execute(CLEAR_DB_QUERY);
			tx.success();
		}
	}

	public static void clearConstraints() {
		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();
		try ( Transaction tx = graphDb.beginTx() ) {
			final Schema schema = graphDb.schema();
			for (ConstraintDefinition cd : schema.getConstraints()) {
				cd.drop();
			}
			tx.success();
		}
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
