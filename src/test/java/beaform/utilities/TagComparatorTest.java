package beaform.utilities;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import beaform.entities.FormulaTag;

/**
 * Test for the formula tag.
 * @author Steven Post
 *
 */
public class TagComparatorTest {

	@Test
	public void testDifferentTags() {

		final TagComparator tagComp = new TagComparator();

		final FormulaTag tag1 = new FormulaTag("testa");
		final FormulaTag tag2 = new FormulaTag("testb");

		assertTrue("The tags are expected to have another ordering", tagComp.compare(tag1, tag2) < 0);
		assertTrue("The tags are expected to have another ordering", tagComp.compare(tag2, tag1) > 0);
	}

	@Test
	public void testSameTags() {

		final TagComparator tagComp = new TagComparator();

		final FormulaTag tag1 = new FormulaTag("test");
		final FormulaTag tag2 = new FormulaTag("test");

		assertTrue("The tags are expected to have the same ordering", tagComp.compare(tag1, tag2) == 0);
		assertTrue("The tags are expected to have the same ordering", tagComp.compare(tag2, tag1) == 0);
	}

}
