package beaform.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.IteratorUtils;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.entities.BaseCompound;
import beaform.entities.BaseIngredient;
import beaform.entities.Formula;
import beaform.entities.FormulaIngredient;
import beaform.entities.FormulaTag;
import beaform.entities.Ingredient;
import beaform.entities.InvalidFormulaException;

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
	private static final String NOTES = "notes";

	private static final String FORMULA_COLUMN = "f";
	private static final String FORMULA_BY_TAG = "MATCH (t:FormulaTag { name:{" + FormulaTagDAO.NAME +
					"} })<-[r]-(f:Formula) RETURN " + FORMULA_COLUMN;

	private static final String INGREDIENTS_COLUMN = "i";
	private static final String INGREDIENT_RALATION = "r";
	private static final String LIST_INGREDIENTS = "MATCH (" + INGREDIENTS_COLUMN + ":Formula)" +
					"<-[" + INGREDIENT_RALATION + ":" + RelTypes.HASINGREDIENT + "]" +
					"-(f:Formula { name:{name} })" +
					" RETURN " + INGREDIENTS_COLUMN + "," + INGREDIENT_RALATION;
	private static final String LIST_BASE_INGREDIENTS = "MATCH (" +
					INGREDIENTS_COLUMN + ":" + BaseCompoundDAO.STR_LABEL +
					")<-[" + INGREDIENT_RALATION + ":" + RelTypes.HASINGREDIENT + "]" +
					"-(f:Formula { name:{name} })" +
					" RETURN " + INGREDIENTS_COLUMN + "," + INGREDIENT_RALATION;

	private static final GraphDatabaseService GRAPHDB = GraphDbHandler.getDbService();

	private FormulaDAO() {
		// private constructor, because this is a utility class.
	}

	public static List<Formula> listAllFormulas() {
		final List<Formula> formulas;

		try ( Transaction tx = GRAPHDB.beginTx() ) {
			try ( ResourceIterator<Node> formulaNodes = GRAPHDB.findNodes(LABEL)) {
				formulas = listFromResourceIterator(formulaNodes);
			}
			tx.success();
		}

		return formulas;
	}

	private static List<Formula> listFromResourceIterator(ResourceIterator<Node> formulaNodes) {
		final List<Formula> formulas = new ArrayList<>();

		while ( formulaNodes.hasNext()) {
			final Node formulaNode = formulaNodes.next();
			final Formula formula = nodeToFormula(formulaNode);
			formulas.add(formula);
		}

		return formulas;
	}

	public static Set<Ingredient> listIngredients(final Formula formula) {
		final Set<Ingredient> retList;

		Map<String, Object> parameters = buildQueryParametersFromFormulaName(formula);
		try ( Transaction tx = GRAPHDB.beginTx(); Result result = GRAPHDB.execute(LIST_INGREDIENTS, parameters); ) {
			retList = listIngredientsFromDbResult(result);
			tx.success();
		}
		try ( Transaction tx = GRAPHDB.beginTx(); Result result = GRAPHDB.execute(LIST_BASE_INGREDIENTS, parameters); ) {
			Set<Ingredient> tmpList = listIngredientsFromDbResult(result);
			retList.addAll(tmpList);
			tx.success();
		}

		return retList;
	}

	private static Map<String, Object> buildQueryParametersFromFormulaName(Formula formula) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(NAME, formula.getName());
		return parameters;
	}

	private static Set<Ingredient> listIngredientsFromDbResult(Result result) {
		final Set<Ingredient> retList = new HashSet<>();

		while (result.hasNext()) {
			final Map<String,Object> row = result.next();
			final Ingredient ingredient = getIngredientFromDbRow(row);
			retList.add(ingredient);
		}

		return retList;
	}

	private static Ingredient getIngredientFromDbRow(Map<String,Object> row) {
		Node ingredientNode = (Node) row.get(INGREDIENTS_COLUMN);
		String labelName = "NOT SET";
		for(Label label : ingredientNode.getLabels()) {
			labelName = label.name();
		}

		final Relationship rel = (Relationship) row.get(INGREDIENT_RALATION);
		final String amount = (String) rel.getProperty(RELATION_AMOUNT);
		switch (labelName) {
			case "Formula":
				return formulaNodeToIngredient(ingredientNode, amount);
			case BaseCompoundDAO.STR_LABEL:
				return baseCompoundToIngredient(ingredientNode, amount);
			default:
				throw new UnsupportedOperationException("Unable to convert type " + labelName + " into an ingredient");
		}

	}

	private static Ingredient formulaNodeToIngredient(final Node ingredientNode, final String amount) {
		final Formula ingredientCore = nodeToFormula(ingredientNode);
		return new FormulaIngredient(ingredientCore, amount);
	}

	private static Ingredient baseCompoundToIngredient(final Node ingredientNode, final String amount) {
		final BaseCompound ingredientCore = BaseCompoundDAO.nodeToBaseCompound(ingredientNode);
		return new BaseIngredient(ingredientCore, amount);
	}

	public static void updateExistingInDb(final Formula formula) {

		try ( Transaction tx = GRAPHDB.beginTx() ) {

			Node formNode = findFormulaNodeByName(formula.getName());
			formNode.setProperty(DESCRIPTION, formula.getDescription());
			formNode.setProperty(TOTAL_AMOUNT, formula.getTotalAmount());

			removeAllIngredientsFromFormulaInDb(formNode);

			addTags(IteratorUtils.toList(formula.getTags()), formNode);

			addIngredientsToFormulaNode(formNode, formula.getIngredients());
			tx.success();
		}
	}

	private static Node findFormulaNodeByName(final String name) {
		Node formNode = GRAPHDB.findNode(LABEL, NAME, name);
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

	private static void addIngredientsToFormulaNode(final Node formula, final Set<Ingredient> ingredients) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Adding " + ingredients.size() + " ingredient(s) to " + (String) formula.getProperty(NAME));
		}

		for (final Ingredient ingredient : ingredients) {
			addIngredientToFormulaNode(formula, ingredient);
		}
	}

	private static void addIngredientToFormulaNode(final Node formula, final Ingredient ingredient) {
		if (ingredient instanceof FormulaIngredient) {
			addFormulaIngredient(formula, ingredient);
		}
		else if (ingredient instanceof BaseIngredient) {
			addBaseIngredient(formula, ingredient);
		}
		else {
			throw new UnsupportedOperationException("Only formula ingredients can be persisted");
		}
	}

	private static void addFormulaIngredient(final Node formula, final Ingredient ingredient) {
		final FormulaIngredient formIngr = (FormulaIngredient) ingredient;
		Node ingredientNode;
		try {
			ingredientNode = findFormulaNodeByName(formIngr.getFormula().getName());
		}
		catch (NoSuchFormulaException e) {
			LOG.debug("Unable to find the ingredient in the DB, creating it: " + e.getMessage(), e);
			Formula ingredientFormula = formIngr.getFormula();
			ingredientNode = addFormula(ingredientFormula);
		}
		Relationship relation = formula.createRelationshipTo(ingredientNode, RelTypes.HASINGREDIENT);
		relation.setProperty(RELATION_AMOUNT, ingredient.getAmount());
	}

	private static void addBaseIngredient(final Node formula, final Ingredient ingredient) {
		final BaseIngredient baseIngredient = (BaseIngredient) ingredient;
		Node ingredientNode = BaseCompoundDAO.findOrCreate(baseIngredient.getBase());

		Relationship relation = formula.createRelationshipTo(ingredientNode, RelTypes.HASINGREDIENT);
		relation.setProperty(RELATION_AMOUNT, ingredient.getAmount());
	}

	public static Node addFormula(final Formula formula) {
		final Node formNode;

		try ( Transaction tx = GRAPHDB.beginTx() ) {

			formNode = GRAPHDB.createNode(LABEL);
			formNode.setProperty(NAME, formula.getName());
			formNode.setProperty(DESCRIPTION, formula.getDescription());
			formNode.setProperty(TOTAL_AMOUNT, formula.getTotalAmount());
			formNode.setProperty(NOTES, formula.getNotes());

			addTags(IteratorUtils.toList(formula.getTags()), formNode);
			addIngredientsToFormulaNode(formNode, formula.getIngredients());

			tx.success();
		}

		return formNode;

	}

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
	 * @return the found {@link Formula}
	 * @throws NoSuchFormulaException if no formula was found.
	 */
	public static Formula findFormulaByName(final String name) {

		Formula formula;

		try ( Transaction tx = GRAPHDB.beginTx() ) {

			Node formulaNode = GRAPHDB.findNode(LABEL, NAME, name);
			if (formulaNode == null) {
				throw new NoSuchFormulaException("Formula '" + name + "' doesn't seem to exist");
			}
			formula = nodeToFormula(formulaNode);
			fillTagsForFormulaFromDB(formula, formulaNode);

			tx.success();
		}

		return formula;
	}

	public static List<Formula> findFormulasByTag(final String tagName) {

		final List<Formula> formulas;

		try ( Transaction tx = GRAPHDB.beginTx() ) {

			Map<String, Object> parameters = buildQueryParametersFromTagName(tagName);
			try (ResourceIterator<Node> resultIterator = GRAPHDB.execute(FORMULA_BY_TAG, parameters).columnAs(FORMULA_COLUMN)) {
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
		for (Relationship relation : relations) {
			Node tagNode = relation.getEndNode();
			FormulaTag tag = FormulaTagDAO.nodeToTag(tagNode);
			formula.addTag(tag);
		}
	}

	public static Formula nodeToFormula(Node formulaNode) {

		if (!formulaNodeHasCorrectLabel(formulaNode)) {
			throw new InvalidFormulaException();
		}

		String name = (String) formulaNode.getProperty(NAME);
		String description = (String) formulaNode.getProperty(DESCRIPTION);
		String totalAmount = (String) formulaNode.getProperty(TOTAL_AMOUNT);
		String notes = (String) formulaNode.getProperty(NOTES);

		final Formula returnFormula = new Formula(name, description, totalAmount);
		returnFormula.setNotes(notes);
		return returnFormula;
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
