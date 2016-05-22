package beaform;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import beaform.entities.Formula;
import beaform.entities.Ingredient;
import beaform.gui.TreeViewFormula;

/**
 * A testing class for the {@link TreeViewFormula}
 *
 * @author Steven Post
 *
 */
public class TreeViewFormulaTest {

	/**
	 * Test a simple formula
	 */
	@Test
	public void testSimpleFormula() {
		final Formula form = new Formula();
		form.setName("TestFormula");
		form.setDescription("Test formula");
		final TreeViewFormula testObj = new TreeViewFormula(form);

		assertEquals("TestFormula", testObj.getFormula().getName());
		assertEquals("", testObj.getAmount());

	}

	/**
	 * Test a formula with ingredients.
	 */
	@Test
	public void testIngredient() {
		final Formula form = new Formula();
		form.setName("TestFormula");
		form.setDescription("Test formula");
		final Ingredient ingredient = new Ingredient(form, "50%");
		final TreeViewFormula testObj = new TreeViewFormula(ingredient);

		assertEquals("TestFormula", testObj.getFormula().getName());
		assertEquals("50%", testObj.getAmount());

	}

}
