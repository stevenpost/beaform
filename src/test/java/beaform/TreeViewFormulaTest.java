package beaform;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import beaform.entities.Formula;
import beaform.gui.TreeViewFormula;

public class TreeViewFormulaTest {

	@Test
	public void testSimpleFormula() {
		Formula form = new Formula();
		form.setName("TestFormula");
		form.setDescription("Test formula");
		TreeViewFormula testObj = new TreeViewFormula(form, "");

		assertEquals("TestFormula", testObj.getFormula().getName());
		assertEquals("", testObj.getMetadata());

	}

	@Test
	public void testIngredient() {
		Formula form = new Formula();
		form.setName("TestFormula");
		form.setDescription("Test formula");
		TreeViewFormula testObj = new TreeViewFormula(form, "TestFormula|50%");

		assertEquals("TestFormula", testObj.getFormula().getName());
		assertEquals("50%", testObj.getMetadata());

	}

}
