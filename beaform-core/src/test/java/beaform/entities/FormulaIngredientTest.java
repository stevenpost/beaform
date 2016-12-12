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
public class FormulaIngredientTest {

	@Test
	public void testEquals() {
		final FormulaIngredient ingr1 = new FormulaIngredient(new Formula(), "10%");
		final FormulaIngredient ingr2 = new FormulaIngredient(new Formula(), "10%");
		assertEquals("The ingredients are not equal", ingr1, ingr2);
	}

	@Test
	public void testEqualsSameObject() {
		final FormulaIngredient ingr = new FormulaIngredient(new Formula(), "40%");
		assertEquals("The ingredients are not equal", ingr, ingr);
	}

	@Test
	public void testEqualsDifferentType() {
		final FormulaIngredient ingr = new FormulaIngredient(new Formula(), "50%");
		assertFalse("The ingredients are aqual", ingr.equals(new Object()));
	}

	@Test
	public void testNotEqual() {
		final FormulaIngredient tag1 = new FormulaIngredient(new Formula(), "test1");
		final FormulaIngredient tag2 = new FormulaIngredient(new Formula("testform", "long description", "100g"), "test1");
		assertFalse("The ingredients are equal", tag1.equals(tag2));
	}

	@Test
	public void testEqualsHash() {
		final FormulaIngredient tag1 = new FormulaIngredient(new Formula(), "20%");
		final FormulaIngredient tag2 = new FormulaIngredient(new Formula(), "20%");
		assertEquals("The ingredients are not equal", tag1.hashCode(), tag2.hashCode());
	}

	@Test
	public void testNotEqualsHash() {
		final FormulaIngredient tag1 = new FormulaIngredient(new Formula(), "30%");
		final FormulaIngredient tag2 = new FormulaIngredient(new Formula("testform", "long description", "100g"), "30%");
		assertNotSame("The ingredients are equal", Integer.valueOf(tag1.hashCode()), Integer.valueOf(tag2.hashCode()));
	}

	@Test
	public void testToString() {
		final FormulaIngredient tag = new FormulaIngredient(new Formula("testform", "long description", "100g"), "30%");
		assertEquals("Not the expected result for toString()", "testform [30%]", tag.toString());
	}

}
