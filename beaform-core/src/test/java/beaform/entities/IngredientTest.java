package beaform.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

/**
 * Test for the formula tag.
 * @author Steven Post
 *
 */
@SuppressWarnings("static-method")
public class IngredientTest {

	@Test
	public void testEquals() {
		final Ingredient ingr1 = new Ingredient(new Formula(), "10%");
		final Ingredient ingr2 = new Ingredient(new Formula(), "10%");
		assertEquals("The ingredients are not equal", ingr1, ingr2);
	}

	@Test
	public void testEqualsSameObject() {
		final Ingredient ingr = new Ingredient(new Formula(), "40%");
		assertEquals("The ingredients are not equal", ingr, ingr);
	}

	@Test
	public void testEqualsDifferentType() {
		final Ingredient ingr = new Ingredient(new Formula(), "50%");
		assertFalse("The ingredients are aqual", ingr.equals(new Object()));
	}

	@Test
	public void testNotEqual() {
		final Ingredient tag1 = new Ingredient(new Formula(), "test1");
		final Ingredient tag2 = new Ingredient(new Formula("testform", "long description", "100g"), "test1");
		assertFalse("The ingredients are equal", tag1.equals(tag2));
	}

	@Test
	public void testEqualsHash() {
		final Ingredient tag1 = new Ingredient(new Formula(), "20%");
		final Ingredient tag2 = new Ingredient(new Formula(), "20%");
		assertEquals("The ingredients are not equal", tag1.hashCode(), tag2.hashCode());
	}

	@Test
	public void testNotEqualsHash() {
		final Ingredient tag1 = new Ingredient(new Formula(), "30%");
		final Ingredient tag2 = new Ingredient(new Formula("testform", "long description", "100g"), "30%");
		assertNotSame("The ingredients are equal", tag1.hashCode(), tag2.hashCode());
	}

	@Test
	public void testToString() {
		final Ingredient tag = new Ingredient(new Formula("testform", "long description", "100g"), "30%");
		assertEquals("Not the expected result for toString()", "testform [30%]", tag.toString());
	}

}
