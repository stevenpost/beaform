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

	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String TOTAL_AMOUNT = "totalAmount";
	private static final String RELATION_AMOUNT = "amount";

	private static final String FORMULA_COLUMN = "f";
	private static final String FORMULA_BY_TAG = "MATCH (t:FormulaTag { name:{" + FormulaTagDAO.NAME +
					"} })<-[r]-(f:Formula) RETURN " + FORMULA_COLUMN;

	private static final String INGREDIENTS_COLUMN = "i";
	private static final String INGREDIENT_RALATION = "r";
	private static final String LIST_INGREDIENTS = "MATCH (i:Formula)<-[r:" + RelTypes.HASINGREDIENT +
					"]-(f:Formula { name:{name} }) RETURN " + INGREDIENTS_COLUMN + "," + INGREDIENT_RALATION;

	private FormulaDAO() {
		// private constructor, because this is a utility class.
	}

	public static List<Ingredient> listIngredients(final Formula formula) {
		final List<Ingredient> retList;

		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();

		Map<String, Object> parameters = buildQueryParametersFromFormulaName(formula);
		try ( Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(LIST_INGREDIENTS, parameters); ) {
			retList = listIngredientsFromDbResult(result);
			tx.success();
		}

		return retList;
	}

	private static Map<String, Object> buildQueryParametersFromFormulaName(Formula formula) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(NAME, formula.getName());
		return parameters;
	}

	private static List<Ingredient> listIngredientsFromDbResult(Result result) {
		final List<Ingredient> retList = new ArrayList<>();

		while (result.hasNext()) {
			final Map<String,Object> row = result.next();
			final Ingredient ingredient = getIngredientFromDbRow(row);
			retList.add(ingredient);
		}

		return retList;
	}

	private static Ingredient getIngredientFromDbRow(Map<String,Object> row) {
		final Formula ingredientCore = nodeToFormula((Node) row.get(INGREDIENTS_COLUMN));
		final Relationship rel = (Relationship) row.get(INGREDIENT_RALATION);
		final String amount = (String) rel.getProperty(RELATION_AMOUNT);
		return new Ingredient(ingredientCore, amount);
	}

	public static void updateExistingInDb(final Formula formula) throws NoSuchFormulaException {

		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();

		try ( Transaction tx = graphDb.beginTx() ) {

			Node formNode = findFormulaNodeByName(formula.getName(), graphDb);
			formNode.setProperty(DESCRIPTION, formula.getDescription());
			formNode.setProperty(TOTAL_AMOUNT, formula.getTotalAmount());

			removeAllIngredientsFromFormulaInDb(formNode);

			addTags(IteratorUtils.toList(formula.getTags()), formNode);

			addIngredientsToFormulaNode(formNode, formula.getIngredients());
			tx.success();
		}
	}

	private static Node findFormulaNodeByName(final String name,
	                                          final GraphDatabaseService graphDb) throws NoSuchFormulaException {
		Node formNode = graphDb.findNode(LABEL, NAME, name);
		if (formNode == null) {
			throw new NoSuchFormulaException("A Formula with the name '" + name + "' does not exist");
		}
		return formNode;
	}

	private static void removeAllIngredientsFromFormulaInDb(Node formNode) {
		Iterator<Relationship> itRel = formNode.getRelationships(Direction.OUTGOING, RelTypes.HASINGREDIENT).iterator();
		while (itRel.hasNext()) {
			Relationship relation = itRel.next();
			relation.delete();
		}
	}

	private static void addIngredientsToFormulaNode(final Node formula, final List<Ingredient> ingredients) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Adding " + ingredients.size() + " ingredient(s) to " + (String) formula.getProperty(NAME));
		}

		for (final Ingredient ingredient : ingredients) {
			addIngredientToFormulaNode(formula, ingredient);
		}
	}

	private static void addIngredientToFormulaNode(final Node formula, final Ingredient ingredient) {
		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();

		Node ingredientNode;
		try {
			ingredientNode = findFormulaNodeByName(ingredient.getFormula().getName(), graphDb);
		}
		catch (NoSuchFormulaException e) {
			LOG.debug("Unable to find the ingredient in the DB, creating it: " + e.getMessage(), e);
			Formula ingredientFormula = ingredient.getFormula();
			ingredientNode = addFormula(ingredientFormula);
		}
		Relationship relation = formula.createRelationshipTo(ingredientNode, RelTypes.HASINGREDIENT);
		relation.setProperty(RELATION_AMOUNT, ingredient.getAmount());
	}

	public static Node addFormula(final Formula formula) {

		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();
		final Node formNode;

		try ( Transaction tx = graphDb.beginTx() ) {

			formNode = graphDb.createNode(LABEL);
			formNode.setProperty(NAME, formula.getName());
			formNode.setProperty(DESCRIPTION, formula.getDescription());
			formNode.setProperty(TOTAL_AMOUNT, formula.getTotalAmount());

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

		if (!formulaHasTag(formula, tag)) {
			formula.createRelationshipTo(tagNode, RelTypes.HASTAG);
		}
	}

	private static boolean formulaHasTag(final Node formula, final FormulaTag tag) {
		Iterable<Relationship> relations = formula.getRelationships(Direction.OUTGOING, RelTypes.HASTAG);
		for (Relationship relation : relations) {
			String name = (String) relation.getEndNode().getProperty(FormulaTagDAO.NAME);
			if (tag.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method finds a formula in the DB based on a name.
	 *
	 * @param name the name of the formula to look for
	 * @return the found {@link Formula} or null if none was found.
	 */
	public static Formula findFormulaByName(final String name) {

		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();
		Formula formula;

		try ( Transaction tx = graphDb.beginTx() ) {

			Node formulaNode = graphDb.findNode(LABEL, NAME, name);
			if (formulaNode == null) {
				return null;
			}
			formula = nodeToFormula(formulaNode);
			fillTagsForFormulaFromDB(formula, formulaNode);

			tx.success();
		}

		return formula;
	}

	public static List<Formula> findFormulasByTag(final String tagName) {

		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();
		final List<Formula> formulas;

		try ( Transaction tx = graphDb.beginTx() ) {

			Map<String, Object> parameters = buildQueryParametersFromTagName(tagName);
			try (ResourceIterator<Node> resultIterator = graphDb.execute(FORMULA_BY_TAG, parameters).columnAs(FORMULA_COLUMN)) {
				formulas = listFormulasFromDbResult(resultIterator);
			}

			tx.success();
		}

		return formulas;
	}

	private static List<Formula> listFormulasFromDbResult(ResourceIterator<Node> resultIterator) {
		List<Formula> formulas = new ArrayList<>();
		while (resultIterator.hasNext()) {
			Node formulaNode = resultIterator.next();
			Formula form = nodeToFormula(formulaNode);
			fillTagsForFormulaFromDB(form, formulaNode);
			formulas.add(form);
		}
		return formulas;
	}

	private static Map<String, Object> buildQueryParametersFromTagName(String tag) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(FormulaTagDAO.NAME, tag);
		return parameters;
	}

	private static void fillTagsForFormulaFromDB(Formula formula, Node formulaNode) {
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

	public static Formula nodeToFormula(Node formulaNode) {

		if (!formulaNodeHasCorrectLabel(formulaNode)) {
			throw new InvalidFormulaException();
		}

		String name = (String) formulaNode.getProperty(NAME);
		String description = (String) formulaNode.getProperty(DESCRIPTION);
		String totalAmount = (String) formulaNode.getProperty(TOTAL_AMOUNT);

		return new Formula(name, description, totalAmount);
	}

	private static boolean formulaNodeHasCorrectLabel(Node formulaNode) {
		final Iterable<Label> labelIt = formulaNode.getLabels();
		for (Label label : labelIt) {
			if (label.name().equals(LABEL.name())) {
				return true;
			}
		}
		return false;
	}

}
