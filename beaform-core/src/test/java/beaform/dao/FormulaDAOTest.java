package beaform.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.ConstraintViolationException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.debug.DebugUtils;
import beaform.entities.BaseCompound;
import beaform.entities.BaseIngredient;
import beaform.entities.Formula;
import beaform.entities.Ingredient;
import beaform.entities.InvalidFormulaException;
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
		GraphDbHandler.initInstanceWithDbPath("neo4j_test/db");
	}

	@Test
	public void testGetIngredients() throws Exception {
		DebugUtils.fillDb();
		final Callable<Formula> task = new SearchFormulaTask("Form1");
		final Formula result = task.call();
		final Set<Ingredient> ingredients = FormulaDAO.listIngredients(result);
		for (Ingredient ingredient : ingredients) {
			LOG.debug("Ingredient: " + ingredient.toString());
		}
		assertEquals("The ingredient list isn't the expected size", 1, ingredients.size());
	}

	@Test
	public void testPersistBaseIngredient() {
		final String formName = "dark water";
		final BaseCompound water = new BaseCompound("Water");
		final BaseCompound colouring = new BaseCompound("Colouring");
		final Formula formulaToPersist = new Formula(formName, "testDesc", "100g");
		final Ingredient ingredient = new BaseIngredient(water, "95%");
		final Ingredient darkIngredient = new BaseIngredient(colouring, "5%");
		formulaToPersist.addIngredient(ingredient);
		formulaToPersist.addIngredient(darkIngredient);

		FormulaDAO.addFormula(formulaToPersist);

		final Formula resultingFormula = FormulaDAO.findFormulaByName(formName);
		Set<Ingredient> ingredients = FormulaDAO.listIngredients(resultingFormula);
		resultingFormula.addAllIngredients(ingredients);

		assertEquals("The 2 formulas are not the same", formulaToPersist, resultingFormula);

	}

	@Test
	public void testPersistFullFormula() {
		final Formula formulaToPersist = new Formula("testName", "testDesc", "100g");
		formulaToPersist.setNotes("Testing notes");

		FormulaDAO.addFormula(formulaToPersist);

		final Formula resultingFormula = FormulaDAO.findFormulaByName("testName");
		assertEquals("The 2 formulas are not the same", formulaToPersist, resultingFormula);
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
		final Formula formula = new Formula("Form1", "New description", "100g");
		FormulaDAO.updateExistingInDb(formula);
		final Callable<Formula> task = new SearchFormulaTask("Form1");
		final Formula result = task.call();
		assertEquals("This isn't the expected description", "New description", result.getDescription());
	}

	@Test(expected=NoSuchFormulaException.class)
	public void testUpdateExistingNotFound() throws Exception {
		final Formula formula = new Formula("Form1", "New description", "100g");
		FormulaDAO.updateExistingInDb(formula);
		final Callable<Formula> task = new SearchFormulaTask("Form1");
		final Formula result = task.call();
		assertEquals("This isn't the expected description", "New description", result.getDescription());
	}

	@Test(expected=InvalidFormulaException.class)
	public void testNodeToTagWithoutLabel() {
		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();
		try (Transaction tx = graphDb.beginTx()) {
			Node node = graphDb.createNode();
			FormulaDAO.nodeToFormula(node);
			tx.success();
		}
	}

	@Test(expected=InvalidFormulaException.class)
	public void testNodeToTagWithInvalidLabel() {
		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();
		try (Transaction tx = graphDb.beginTx()) {
			Node node = graphDb.createNode(Label.label("dummylabel"));
			FormulaDAO.nodeToFormula(node);
			tx.success();
		}
	}

	@Test
	public void testListAllFormulas() {
		List<Formula> formulas = FormulaDAO.listAllFormulas();
		assertEquals("This isn't the exted number of formulas in the DB", 0, formulas.size());
	}

	@Test
	public void testListAllFormulasFilledDb() {
		DebugUtils.fillDb();
		List<Formula> formulas = FormulaDAO.listAllFormulas();
		assertEquals("This isn't the exted number of formulas in the DB", 4, formulas.size());
	}

	@Test(expected = ConstraintViolationException.class)
	public void testDuplicateFormulas() {
		final Formula formulaToPersist = new Formula("testName", "testDesc", "100g");
		final Formula formulaToPersist2 = new Formula("testName", "", "");

		FormulaDAO.addFormula(formulaToPersist);
		FormulaDAO.addFormula(formulaToPersist2);
	}

	@After
	public void tearDown() {
		DebugUtils.clearDb();
	}

}
