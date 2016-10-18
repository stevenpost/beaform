package beaform.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.collections.ListUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.debug.DebugUtils;
import beaform.entities.Formula;
import beaform.entities.FormulaTag;
import beaform.entities.Ingredient;
import beaform.search.SearchFormulaTask;

/**
 * Test for the formula DAO.
 *
 * @author Steven Post
 *
 */
@SuppressWarnings("static-method")
public class FormulaDAOTest {

	private static final Logger LOG = LoggerFactory.getLogger(FormulaDAOTest.class);

	@Before
	public void setUp() {
		GraphDbHandler.initInstance("neo4j_test/db");
		DebugUtils.clearDb();
	}

	@Test
	public void testAddFormula() throws Exception {
		FormulaDAO.addFormula("Testform", "Description", "100g", ListUtils.EMPTY_LIST, ListUtils.EMPTY_LIST);
		final Callable<Formula> task = new SearchFormulaTask("Testform");
		final Formula result = task.call();
		assertNotNull("No result found", result);
	}

	@Test
	public void testAddFormulaWithContent() throws Exception {
		final List<FormulaTag> tags = new ArrayList<>();
		tags.add(new FormulaTag("testtag"));
		final List<Ingredient> ingredients = new ArrayList<>();
		ingredients.add(new Ingredient(new Formula("TestForm", "Some desc", "100%"), "100%"));
		FormulaDAO.addFormula("Testform", "Description", "100g", ingredients, tags);
		final Callable<Formula> task = new SearchFormulaTask("Testform");
		final Formula result = task.call();
		assertNotNull("Unable to find the newly created formula", result);
	}

	@Test
	public void testGetIngredients() throws Exception {
		DebugUtils.fillDb();
		final Callable<Formula> task = new SearchFormulaTask("Form1");
		final Formula result = task.call();
		final List<Ingredient> ingredients = FormulaDAO.getIngredients(result);
		for (Ingredient ingredient : ingredients) {
			LOG.debug("Ingredient: " + ingredient.toString());
		}
		assertEquals("The ingredient list isn't the expected size", 1, ingredients.size());
	}

	@Test
	public void testFindFormulaByName() {
		DebugUtils.fillDb();
		final Formula formula = FormulaDAO.findFormulaByName("Form1");
		assertNotNull("The formula wasn't found", formula);
		assertEquals("This isn't the expected formula", "Form1", formula.getName());
	}

	@Test
	public void testFindFormulasByTag() {
		DebugUtils.fillDb();
		final List<Formula> formulas = FormulaDAO.findFormulasByTag("First");
		assertEquals("This isn't the number of found formulas", 2, formulas.size());
	}

	@Test
	public void testUpdateExisting() throws Exception {
		DebugUtils.fillDb();
		FormulaDAO.updateExisting("Form1", "New description", "100g", Collections.emptyList(), Collections.emptyList());
		final Callable<Formula> task = new SearchFormulaTask("Form1");
		final Formula result = task.call();
		assertEquals("This isn't the expected description", "New description", result.getDescription());
	}

	@Test(expected=NoSuchFormulaException.class)
	public void testUpdateExistingNotFound() throws Exception {
		FormulaDAO.updateExisting("Form1", "New description", "100g", Collections.emptyList(), Collections.emptyList());
		final Callable<Formula> task = new SearchFormulaTask("Form1");
		final Formula result = task.call();
		assertEquals("This isn't the expected description", "New description", result.getDescription());
	}

	@Test(expected=InvalidFormulaException.class)
	public void testNodeToTagWithoutLabel() {
		final GraphDatabaseService graphDb = GraphDbHandler.getInstance().getService();
		try (Transaction tx = graphDb.beginTx()) {
			Node node = graphDb.createNode();
			FormulaDAO.nodeToFormula(node);
			tx.success();
		}
	}

	@Test(expected=InvalidFormulaException.class)
	public void testNodeToTagWithInvalidLabel() {
		final GraphDatabaseService graphDb = GraphDbHandler.getInstance().getService();
		try (Transaction tx = graphDb.beginTx()) {
			Node node = graphDb.createNode(Label.label("dummylabel"));
			FormulaDAO.nodeToFormula(node);
			tx.success();
		}
	}

	@After
	public void tearDown() {
		DebugUtils.clearDb();
	}

}
