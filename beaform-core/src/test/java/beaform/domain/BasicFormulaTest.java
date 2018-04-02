package beaform.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class BasicFormulaTest {

	private static final String FORMULA_NAME = "testformula";
	private static final String FORMULA_DESCRIPTION = "Testing description";
	private Formula testFormula;

	@Before
	public void setUp() {
		this.testFormula = FormulaFactory.createFormula(FORMULA_NAME, FORMULA_DESCRIPTION);
	}

	@Test
	public void testFormulaCreation() {
		assertNotNull("The formula doesn't seem to be created", this.testFormula);
	}
	@Test
	public void testFormulaName() {
		assertEquals("The name seems incorrect", FORMULA_NAME, this.testFormula.getName());
	}

	@Test
	public void testDescription() {
		assertEquals("The description seems incorrect", FORMULA_DESCRIPTION, this.testFormula.getDescription());
	}

	@Test
	public void testIngredients() {
		Set<Ingredient> ingredients = this.testFormula.getIngredients();
		assertEquals("This isn't the number of ingredients I was expecting", 0, ingredients.size());
	}

}
