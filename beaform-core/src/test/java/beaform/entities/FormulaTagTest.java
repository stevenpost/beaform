package beaform.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test for the formula tag.
 * @author Steven Post
 *
 */
@SuppressWarnings("static-method")
public class FormulaTagTest {

	@Test
	public void testEquals() {
		final FormulaTag tag1 = new FormulaTag("test");
		final FormulaTag tag2 = new FormulaTag("test");
		assertEquals("The tags are not equal", tag1, tag2);
	}

	@Test
	public void testEqualsSameObject() {
		final FormulaTag tag1 = new FormulaTag("test");
		assertTrue("The tags are not equal", tag1.equals(tag1));
	}

	@Test
	public void testNotEqual() {
		final FormulaTag tag1 = new FormulaTag("test1");
		final FormulaTag tag2 = new FormulaTag("test2");
		assertFalse("The tags are equal", tag1.equals(tag2));
	}

	@Test
	public void testNotEqualDifferentType() {
		final FormulaTag tag1 = new FormulaTag("test1");
		final Object tag2 = new Object();
		assertFalse("The tags are equal", tag1.equals(tag2));
	}

	@Test
	public void testEqualsHash() {
		final FormulaTag tag1 = new FormulaTag("test");
		final FormulaTag tag2 = new FormulaTag("test");
		assertEquals("The tags are not equal", tag1.hashCode(), tag2.hashCode());
	}

	@Test
	public void testNotEqualsHash() {
		final FormulaTag tag1 = new FormulaTag("test1");
		final FormulaTag tag2 = new FormulaTag("test2");
		assertFalse("The tags are equal", tag1.hashCode() == tag2.hashCode());
	}

}
