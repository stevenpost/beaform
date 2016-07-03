package beaform;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
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

	private static final String AMOUNT = "50%";
	private static final String NAME = "TestFormula";

	private Formula form;

	@Before
	public void setUp() {
		this.form = new Formula(NAME, "Test formula", "0%");
	}

	@Test
	public void testEmptyAmount() {
		final TreeViewFormula testObj = new TreeViewFormula(this.form);
		assertEquals("The amount is not empty.", "", testObj.getAmount());

	}

	@Test
	public void testWithIngredient() {
		final Ingredient ingredient = new Ingredient(this.form, AMOUNT);
		final TreeViewFormula testObj = new TreeViewFormula(ingredient);

		assertEquals("The ingredient amount is not as expected.", AMOUNT, testObj.getAmount());

	}

}
