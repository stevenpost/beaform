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

	@Before
	public void setUp() {
		GraphDbHandler.initInstance("test");
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
		assertNotNull(result);
	}

	@Test
	public void testGetIngredients() throws Exception {
		DebugUtils.fillDb();
		final Callable<Formula> task = new SearchFormulaTask("Form1");
		final Formula result = task.call();
		final List<Ingredient> ingredients = FormulaDAO.getIngredients(result);
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

	@After
	public void tearDown() {
		DebugUtils.clearDb();
		GraphDbHandler.clearInstance();
	}

}