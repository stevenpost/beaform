package beaform.entities;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Test for the formula tag.
 * @author Steven Post
 *
 */
public class FormulaTagTest extends TestCase {

	@Test
	public static void testEquals() {
		final FormulaTag tag1 = new FormulaTag("test");
		final FormulaTag tag2 = new FormulaTag("test");
		assertEquals("The tags are not equal", tag1, tag2);
	}

	@Test
	public static void testEqualsSameObject() {
		final FormulaTag tag1 = new FormulaTag("test");
		assertTrue("The tags are not equal", tag1.equals(tag1));
	}

	@Test
	public static void testNotEqual() {
		final FormulaTag tag1 = new FormulaTag("test1");
		final FormulaTag tag2 = new FormulaTag("test2");
		assertFalse("The tags are equal", tag1.equals(tag2));
	}

	@Test
	public static void testNotEqualDifferentType() {
		final FormulaTag tag1 = new FormulaTag("test1");
		final Object tag2 = new Object();
		assertFalse("The tags are equal", tag1.equals(tag2));
	}

	/**
	 * Test hash.
	 */
	@Test
	public static void testEqualsHash() {
		final FormulaTag tag1 = new FormulaTag("test");
		final FormulaTag tag2 = new FormulaTag("test");
		assertEquals("The tags are not equal", tag1.hashCode(), tag2.hashCode());
	}

	/**
	 * Test not equals.
	 */
	@Test
	public static void testNotEqualsHash() {
		final FormulaTag tag1 = new FormulaTag("test1");
		final FormulaTag tag2 = new FormulaTag("test2");
		assertFalse("The tags are equal", tag1.hashCode() == tag2.hashCode());
	}

}
