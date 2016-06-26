package beaform.entities;

import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * Test the formula entity.
 * @author Steven Post
 *
 */
public class FormulaTest extends TestCase {

	/** The formula to check. */
	private Formula formula;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Before
	public void setUp() {
		this.formula = new Formula("name", "Description is long", "100g");
	}

	/**
	 * Check the description.
	 */
	@Test
	public void testDescription() {
		assertEquals("This isn't the expected description", "Description is long", this.formula.getDescription());
	}

	/**
	 * Check the name.
	 */
	@Test
	public void testName() {
		assertEquals("This isn't the expected name", "name", this.formula.getName());
	}

	/**
	 * Check the total amount.
	 */
	@Test
	public void testTotalAmount() {
		assertEquals("This isn't the expected amount", "100g", this.formula.getTotalAmount());
	}

	/**
	 * Test for a tag.
	 */
	@Test
	public void testAddTag() {
		this.formula.addTag(new FormulaTag("tagje"));
		final List<FormulaTag> tags = IteratorUtils.toList(this.formula.getTags());
		assertEquals("This isn't the expected amount of tags.", 1, tags.size());
	}

	/**
	 * Test for getting tags as strings.
	 */
	@Test
	public void testGetTagsAsStrings() {
		this.formula.addTag(new FormulaTag("tagje2"));
		final List<String> tags = this.formula.getTagsAsStrings();
		assertEquals("This isn't the expected tag name.", "tagje2", tags.get(0));
	}

	/**
	 * Test for getting tags as strings.
	 */
	@Test
	public void testMultipleGetTagsAsStrings() {
		this.formula.addTag(new FormulaTag("tagje"));
		this.formula.addTag(new FormulaTag("more"));
		final List<String> tags = this.formula.getTagsAsStrings();
		assertEquals("This isn't the expected amount of tags.", 2, tags.size());
	}

	/**
	 * Test the string representation of a formula.
	 */
	@Test
	public void testToStrings() {
		final String expectedString = "name | Description is long | [tagje]";
		this.formula.addTag(new FormulaTag("tagje"));
		final String actualString = this.formula.toString();
		assertEquals("This isn't the expected amount of tags.", expectedString, actualString);
	}

	/**
	 * Test for the equals method.
	 */
	@Test
	public void testEquals() {
		final Formula form1 = new Formula("testform", "Desc", "100g");
		final Formula form2 = new Formula("testform", "Desc", "100g");

		assertEquals("Both formulas aren't equal", form1, form2);
	}

	/**
	 * Test for the equals method.
	 */
	@Test
	public void testNotEquals() {
		final Formula form1 = new Formula("testform", "Description", "100g");
		final Formula form2 = new Formula("testform2", "Description", "100g");

		assertNotSame("Both formulas are equal", form1, form2);
	}

}
