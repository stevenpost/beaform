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

	/** An arbitrary test amount */
	private static final String AMOUNT = "50%";

	/** The name of the test formula */
	private static final String NAME = "TestFormula";

	/** The formula is initialized during test setup */
	private Formula form;

	@Before
	public void setUp() {
		this.form = new Formula(NAME, "Test formula", "0%");
	}

	/**
	 * Test a simple formula
	 */
	@Test
	public void testEmptyAmount() {
		final TreeViewFormula testObj = new TreeViewFormula(this.form);
		assertEquals("The amount is not empty.", "", testObj.getAmount());

	}

	/**
	 * Test a formula with ingredients.
	 */
	@Test
	public void testWithIngredient() {
		final Ingredient ingredient = new Ingredient(this.form, AMOUNT);
		final TreeViewFormula testObj = new TreeViewFormula(ingredient);

		assertEquals("The ingredient amount is not as expected.", AMOUNT, testObj.getAmount());

	}

}
