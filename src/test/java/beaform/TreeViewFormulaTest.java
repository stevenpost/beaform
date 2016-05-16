package beaform;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import beaform.entities.Formula;
import beaform.gui.TreeViewFormula;

public class TreeViewFormulaTest {

	@Test
	public void testSimpleFormula() {
		final Formula form = new Formula();
		form.setName("TestFormula");
		form.setDescription("Test formula");
		TreeViewFormula testObj = new TreeViewFormula(form);

		assertEquals("TestFormula", testObj.getFormula().getName());
		assertEquals("", testObj.getAmount());

	}

	@Test
	public void testIngredient() {
		final Formula form = new Formula();
		form.setName("TestFormula");
		form.setDescription("Test formula");
		Ingredient ingredient = new Ingredient(form, "50%");
		TreeViewFormula testObj = new TreeViewFormula(ingredient);

		assertEquals("TestFormula", testObj.getFormula().getName());
		assertEquals("50%", testObj.getAmount());

	}

}
