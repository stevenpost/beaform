package beaform.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

@SuppressWarnings("static-method")
public class BaseIngredientTest {

	@Test
	public void testAmount() {
		final BaseCompound water = new BaseCompound("Water");
		final BaseIngredient ingredient = new BaseIngredient(water, "10%");

		// Casting, because we want the interface
		assertEquals("This is not the expected amount.", "10%", ((Ingredient)ingredient).getAmount());
		assertEquals("This is not the expected name.", "Water", ingredient.getName());
	}

	@Test
	public void testEquals() {
		final BaseCompound water = new BaseCompound("Water");
		final BaseIngredient tag1 = new BaseIngredient(water, "10%");
		final BaseIngredient tag2 = new BaseIngredient(water, "10%");
		assertEquals("The ingredients are not equal", tag1, tag2);
	}

	@Test
	public void testEqualsSameObject() {
		final BaseCompound water = new BaseCompound("Water");
		final BaseIngredient tag1 = new BaseIngredient(water, "10%");
		assertTrue("The ingredients are not equal", tag1.equals(tag1));
	}

	@Test
	public void testNotEqual() {
		final BaseCompound water = new BaseCompound("Water");
		final BaseCompound darkwater = new BaseCompound("Dark Water");
		final BaseIngredient tag1 = new BaseIngredient(water, "10%");
		final BaseIngredient tag2 = new BaseIngredient(darkwater, "10%");
		assertFalse("The ingredients are equal", tag1.equals(tag2));
	}

	@Test
	public void testNotEqualDifferentType() {
		final BaseCompound water = new BaseCompound("Water");
		final BaseIngredient tag1 = new BaseIngredient(water, "10%");
		final Object tag2 = new Object();
		assertFalse("The ingredients are equal", tag1.equals(tag2));
	}

	@Test
	public void testEqualsHash() {
		final BaseCompound water = new BaseCompound("Water");
		final BaseIngredient tag1 = new BaseIngredient(water, "10%");
		final BaseIngredient tag2 = new BaseIngredient(water, "10%");
		assertEquals("The ingredients are not equal", tag1.hashCode(), tag2.hashCode());
	}

	@Test
	public void testNotEqualsHash() {
		final BaseCompound water = new BaseCompound("Water");
		final BaseCompound darkwater = new BaseCompound("Dark Water");
		final BaseIngredient tag1 = new BaseIngredient(water, "10%");
		final BaseIngredient tag2 = new BaseIngredient(darkwater, "10%");
		assertFalse("The ingredients are equal", tag1.hashCode() == tag2.hashCode());
	}

}
