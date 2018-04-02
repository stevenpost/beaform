package beaform.domain;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

public class FormulaWithIngredientsTest {

	private static final String FORMULA_NAME = "testformula";
	private static final String FORMULA_DESCRIPTION = "Testing description";

	@Test
	public void testAddingIngredient() {
		Formula testFormula = FormulaFactory.createFormula(FORMULA_NAME, FORMULA_DESCRIPTION);
		assertEquals("I wasn't expecting ingredients at this point", 0, testFormula.getIngredients().size());

		Formula ingr1 = FormulaFactory.createFormula("ingr1", "ingredient 1 description");
		testFormula.addIngredient(ingr1, "10g");

		assertEquals("Adding the ingredient doesn't seem to work properly", 1, testFormula.getIngredients().size());
	}

	@Test
	public void testListingIngredients() {
		Formula testFormula = FormulaFactory.createFormula(FORMULA_NAME, FORMULA_DESCRIPTION);

		Formula ingr1 = FormulaFactory.createFormula("ingr1", "ingredient 1 description");
		testFormula.addIngredient(ingr1, "10g");

		Set<Ingredient> ingredients = testFormula.getIngredients();
		for (Ingredient ingredient : ingredients) {
			assertEquals("This isn't the ingredient I was expecting", "ingr1", ingredient.getName());
			assertEquals("This isn't the amount I was expecting", "10g", ingredient.getAmount());
		}
	}


}
