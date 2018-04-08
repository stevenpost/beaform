package beaform.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.IteratorUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the formula entity.
 * @author Steven Post
 *
 */
@SuppressWarnings("static-method")
public class FormulaTest {

	private Formula formula;

	@Before
	public void setUp() {
		this.formula = new Formula("name", "Description is long", "100g");
		this.formula.setNotes("Some long description");
	}

	@Test(expected=InvalidFormulaException.class)
	public void testEmptyName() {
		new Formula("", "Description", "100g");
	}

	@Test(expected=InvalidFormulaException.class)
	public void testNullName() {
		new Formula(null, "Description", "100g");
	}

	@Test
	public void testDescription() {
		assertEquals("This isn't the expected description", "Description is long", this.formula.getDescription());
	}

	@Test
	public void testName() {
		assertEquals("This isn't the expected name", "name", this.formula.getName());
	}

	@Test
	public void testNotes() {
		assertEquals("This isn't the expected description", "Some long description", this.formula.getNotes());
	}

	@Test
	public void testAddIngredients() {
		final Ingredient ingredient = new FormulaIngredient(new Formula("testFormula"), "10%");
		this.formula.addIngredient(ingredient);
		assertFalse("The list of ingredients is empty", this.formula.getIngredients().isEmpty());
	}

	@Test
	public void testAddIngredientObject() {
		final Ingredient ingredient = new FormulaIngredient(new Formula("testFormula"), "10%");
		this.formula.addIngredient(ingredient);
		assertFalse("The list of ingredients is empty", this.formula.getIngredients().isEmpty());
	}

	@Test
	public void testTotalAmount() {
		assertEquals("This isn't the expected amount", "100g", this.formula.getTotalAmount());
	}

	@Test
	public void testAddTag() {
		this.formula.addTag(new FormulaTag("tagje"));
		final List<FormulaTag> tags = IteratorUtils.toList(this.formula.getTags());
		assertEquals("This isn't the expected amount of tags.", 1, tags.size());
	}

	@Test
	public void testGetTagsAsStrings() {
		this.formula.addTag(new FormulaTag("tagje2"));
		final Set<String> tags = this.formula.getTagsAsStrings();
		assertTrue("This isn't the expected tag name.", tags.contains("tagje2"));
	}

	@Test
	public void testMultipleGetTagsAsStrings() {
		this.formula.addTag(new FormulaTag("tagje"));
		this.formula.addTag(new FormulaTag("more"));
		final Set<String> tags = this.formula.getTagsAsStrings();
		assertEquals("This isn't the expected amount of tags.", 2, tags.size());
	}

	@Test
	public void testToStrings() {
		final String expectedString = "name | Description is long | [tagje]";
		this.formula.addTag(new FormulaTag("tagje"));
		final String actualString = this.formula.toString();
		assertEquals("This isn't the expected amount of tags.", expectedString, actualString);
	}

	@Test
	public void testEquals() {
		final Formula form1 = new Formula("testform", "Desc", "100g");
		form1.setNotes("some notes");
		final Formula form2 = new Formula("testform", "Desc", "100g");
		form2.setNotes("some notes");

		assertEquals("Both formulas aren't equal", form1, form2);
	}

	@Test
	public void testNotEquals() {
		final Formula form1 = new Formula("testform", "Description", "100g");
		final Formula form2 = new Formula("testform2", "Description", "100g");

		assertFalse("Both formulas are equal", form1.equals(form2));
	}

	@Test
	public void testNotEqualsOnNotes() {
		final Formula form1 = new Formula("testform", "Desc", "100g");
		form1.setNotes("some notes");
		final Formula form2 = new Formula("testform", "Desc", "100g");
		form2.setNotes("some other notes");

		assertFalse("Both formulas are equal", form1.equals(form2));
	}

}
