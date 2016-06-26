package beaform.entities;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Test for the formula tag.
 * @author Steven Post
 *
 */
public class IngredientTest extends TestCase {

	/**
	 * Test equals.
	 */
	@Test
	public void testEquals() {
		final Ingredient ingr1 = new Ingredient(new Formula(), "10%");
		final Ingredient ingr2 = new Ingredient(new Formula(), "10%");
		assertEquals("The ingredients are not equal", ingr1, ingr2);
	}

	/**
	 * Test not equals.
	 */
	@Test
	public void testNotEqual() {
		final Ingredient tag1 = new Ingredient(new Formula(), "test1");
		final Ingredient tag2 = new Ingredient(new Formula("testform", "long description", "100g"), "test1");
		assertFalse("The ingredients are equal", tag1.equals(tag2));
	}

	/**
	 * Test hash.
	 */
	@Test
	public void testEqualsHash() {
		final Ingredient tag1 = new Ingredient(new Formula(), "20%");
		final Ingredient tag2 = new Ingredient(new Formula(), "20%");
		assertEquals("The ingredients are not equal", tag1.hashCode(), tag2.hashCode());
	}

	/**
	 * Test not equals.
	 */
	@Test
	public void testNotEqualsHash() {
		final Ingredient tag1 = new Ingredient(new Formula(), "30%");
		final Ingredient tag2 = new Ingredient(new Formula("testform", "long description", "100g"), "30%");
		assertNotSame("The ingredients are equal", tag1.hashCode(), tag2.hashCode());
	}

}
