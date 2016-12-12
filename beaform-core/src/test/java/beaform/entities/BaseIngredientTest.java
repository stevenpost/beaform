package beaform.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

@SuppressWarnings("static-method")
public class BaseIngredientTest {

	@Test
	public void testAmount() {
		final BaseIngredient ingredient = new BaseIngredient("Water", "10%");

		// Casting, because we want the interface
		assertEquals("This is not the expected amount.", "10%", ((Ingredient)ingredient).getAmount());
		assertEquals("This is not the expected name.", "Water", ingredient.getName());
	}

}
