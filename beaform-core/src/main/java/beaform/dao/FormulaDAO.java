package beaform.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.entities.Formula;
import beaform.entities.FormulaTag;
import beaform.entities.Ingredient;

/**
 * This class handles all DB access for formulas.
 *
 * @author Steven Post
 *
 */
public final class FormulaDAO {

	private static final Logger LOG = LoggerFactory.getLogger(FormulaDAO.class);

	private static final Label LABEL = Label.label("Formula");

	/** Query to search for a formula by tag */
	private static final String FORMULA_BY_TAG = "MATCH (t:FormulaTag { name:{name} })<-[r]-(f:Formula) RETURN f";

	private static final String LIST_INGREDIENTS = "MATCH (i:Formula)<-[r:" + RelTypes.HASINGREDIENT +
					"]-(f:Formula { name:{name} }) RETURN i,r";

	private FormulaDAO() {
		// private constructor, because this is a utility class.
	}

	public static List<Ingredient> getIngredients(final Formula formula) {
		final List<Ingredient> retList = new ArrayList<>();

		final GraphDatabaseService graphDb = GraphDbHandler.getInstance().getService();

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("name", formula.getName());
		try ( Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(LIST_INGREDIENTS, parameters); ) {

			while (result.hasNext()) {
				final Map<String,Object> row = result.next();

				final Formula ingredientCore = nodeToFormula((Node) row.get("i"));
				final Relationship rel = (Relationship) row.get("r");
				final String amount = (String) rel.getProperty("amount");
				final Ingredient ingredient = new Ingredient(ingredientCore, amount);
				retList.add(ingredient);
			}

			tx.success();
		}

		return retList;
	}

	public static void updateExisting(final String name,
	                                  final String description,
	                                  final String totalAmount,
	                                  final List<Ingredient> ingredients,
	                                  final List<FormulaTag> tags) throws NoSuchFormulaException {

		final GraphDatabaseService graphDb = GraphDbHandler.getInstance().getService();

		try ( Transaction tx = graphDb.beginTx() ) {

			Node formNode = graphDb.findNode(LABEL, "name", name);
			if (formNode == null) {
				throw new NoSuchFormulaException("A Formula with the name '" + name + "' does not exist");
			}
			formNode.setProperty("description", description);
			formNode.setProperty("totalAmount", totalAmount);

			Iterator<Relationship> itRel = formNode.getRelationships(Direction.OUTGOING, RelTypes.HASINGREDIENT).iterator();
			while (itRel.hasNext()) {
				Relationship relation = itRel.next();
				relation.delete();
			}

			addTags(tags, formNode);

			final Formula formula = new Formula(name, description, totalAmount);
			addIngredientsToFormulaNode(formNode, ingredients);
			addIngredientsToFormula(formula, ingredients);
			tx.success();
		}
	}

	private static void addIngredientsToFormulaNode(final Node formula, final List<Ingredient> ingredients) {
		final GraphDatabaseService graphDb = GraphDbHandler.getInstance().getService();

		try ( Transaction tx = graphDb.beginTx() ) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Adding " + ingredients.size() + " ingredient(s) to " + (String) formula.getProperty("name"));
			}

			for (final Ingredient ingredient : ingredients) {
				Node ingredientNode = graphDb.findNode(LABEL, "name", ingredient.getFormula().getName());
				Formula ingredientFormula = ingredient.getFormula();
				if (ingredientNode == null) {
					ingredientNode = addFormula(ingredientFormula);
				}
				Relationship relation = formula.createRelationshipTo(ingredientNode, RelTypes.HASINGREDIENT);
				relation.setProperty("amount", ingredient.getAmount());
			}

			tx.success();
		}
	}

	private static void addIngredientsToFormula(final Formula formula, final List<Ingredient> ingredients) {
		for (final Ingredient ingredient : ingredients) {
			Formula ingredientFormula = ingredient.getFormula();
			formula.addIngredient(ingredientFormula, ingredient.getAmount());
		}
	}

	public static Node addFormula(final String name,
	                              final String description,
	                              final String totalAmount,
	                              final List<Ingredient> ingredients,
	                              final List<FormulaTag> tags) {

		final GraphDatabaseService graphDb = GraphDbHandler.getInstance().getService();
		final Node formNode;

		try ( Transaction tx = graphDb.beginTx() ) {

			final Formula formula = new Formula(name, description, totalAmount);

			formNode = graphDb.createNode(LABEL);
			formNode.setProperty("name", name);
			formNode.setProperty("description", description);
			formNode.setProperty("totalAmount", totalAmount);

			addTags(tags, formNode);
			addIngredientsToFormulaNode(formNode, ingredients);
			addIngredientsToFormula(formula, ingredients);
			tx.success();
		}

		return formNode;

	}

	public static Node addFormula(final Formula formula) {

		final GraphDatabaseService graphDb = GraphDbHandler.getInstance().getService();
		final Node formNode;

		try ( Transaction tx = graphDb.beginTx() ) {

			formNode = graphDb.createNode(LABEL);
			formNode.setProperty("name", formula.getName());
			formNode.setProperty("description", formula.getDescription());
			formNode.setProperty("totalAmount", formula.getTotalAmount());


			addTags(IteratorUtils.toList(formula.getTags()), formNode);
			addIngredientsToFormulaNode(formNode, formula.getIngredients());

			tx.success();
		}

		return formNode;

	}

	/**
	 * This method adds tags to a formula.
	 * It assumes a running transaction.
	 *
	 * @param tags A list of tags
	 * @param entityManager an open entity manager
	 * @param formula the formula to add the tags to
	 * @throws NotSupportedException If the calling thread is already
	 *         associated with a transaction,
	 *         and nested transactions are not supported.
	 * @throws SystemException If the transaction service fails in an unexpected way.
	 */
	private static void addTags(final List<FormulaTag> tags, final Node formula) {
		for (final FormulaTag tag : tags) {
			addTagToFormula(formula, tag);
		}
	}

	private static void addTagToFormula(final Node formula, final FormulaTag tag) {
		Node tagNode = FormulaTagDAO.findOrCreate(tag);

		Iterable<Relationship> relations = formula.getRelationships(Direction.OUTGOING, RelTypes.HASTAG);
		boolean found = false;
		for (Relationship relation : relations) {
			String name = (String) relation.getEndNode().getProperty("name");
			if (tag.getName().equals(name)) {
				found = true;
				break;
			}
		}
		if (!found) {
			formula.createRelationshipTo(tagNode, RelTypes.HASTAG);
		}
	}

	/**
	 * This method finds a formula in the DB based on a name.
	 * It assumes a transaction is already in progress.
	 *
	 * @param name the name of the formula to look for
	 * @return the found {@link Formula} or null if none was found.
	 */
	public static Formula findFormulaByName(final String name) {

		final GraphDatabaseService graphDb = GraphDbHandler.getInstance().getService();
		Formula formula;

		try ( Transaction tx = graphDb.beginTx() ) {

			Node node = graphDb.findNode(LABEL, "name", name);
			if (node == null) {
				return null;
			}
			formula = nodeToFormula(node);
			fillTagsForFormula(formula, node);

			tx.success();
		}

		return formula;
	}

	public static List<Formula> findFormulasByTag(final String tagName) {

		final GraphDatabaseService graphDb = GraphDbHandler.getInstance().getService();
		List<Formula> formulas = new ArrayList<>();

		try ( Transaction tx = graphDb.beginTx() ) {

			Map<String, Object> parameters = new HashMap<>();
			parameters.put("name", tagName);
			try (ResourceIterator<Node> resultIterator = graphDb.execute(FORMULA_BY_TAG, parameters).columnAs("f")) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Got a result");
				}
				while (resultIterator.hasNext()) {
					Node formulaNode = resultIterator.next();
					Formula form = nodeToFormula(formulaNode);
					fillTagsForFormula(form, formulaNode);
					formulas.add(form);
					if (LOG.isDebugEnabled()) {
						LOG.debug("found a mathing formula");
					}
				}
			}

			tx.success();
		}

		return formulas;
	}

	private static void fillTagsForFormula(Formula formula, Node formulaNode) {
		Iterable<Relationship> relations = formulaNode.getRelationships(Direction.OUTGOING, RelTypes.HASTAG);
		int i = 0;
		for (Relationship relation : relations) {
			Node tagNode = relation.getEndNode();
			FormulaTag tag = FormulaTagDAO.nodeToTag(tagNode);
			formula.addTag(tag);
			i++;
			if (LOG.isDebugEnabled()) {
				LOG.debug("found tag number " + i + ": " + tag.toString());
			}
		}
	}

	public static Formula nodeToFormula(Node node) {
		final Iterable<Label> labelIt = node.getLabels();
		boolean hasCorrectLabel = false;
		for (Label label : labelIt) {
			if (label.name().equals(LABEL.name())) {
				hasCorrectLabel = true;
				break;
			}
		}

		if (!hasCorrectLabel) {
			throw new InvalidFormulaException();
		}

		String name = (String) node.getProperty("name");
		String description = (String) node.getProperty("description");
		String totalAmount = (String) node.getProperty("totalAmount");

		return new Formula(name, description, totalAmount);
	}

}
