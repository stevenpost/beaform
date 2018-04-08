package beaform.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * An aroma formula, the core of the whole application.
 *
 * @author Steven Post
 *
 */
public class Formula {

	private String name = "";
	private String notes = "";

	private String description = "";
	private String totalAmount = "";

	private final Set<Ingredient> ingredients = new HashSet<>();

	private final Set<FormulaTag> tags = new HashSet<>();

	public Formula(String name) {
		this(name, "", "");
	}

	public Formula(final String name, final String description, final String totalAmount) {
		if (name == null || "".equals(name)) {
			throw new InvalidFormulaException("The name of a formula cannot be empty");
		}
		this.name = name;
		this.description = description;
		this.totalAmount = totalAmount;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(final String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void addIngredient(final Ingredient ingredient) {
		this.ingredients.add(ingredient);
	}

	public void addAllIngredients(final Collection<Ingredient> ingredientsList) {
		for (Ingredient ingredient : ingredientsList) {
			this.addIngredient(ingredient);
		}
	}

	public Set<Ingredient> getIngredients() {
		final HashSet<Ingredient> returnIngredients = new HashSet<>();
		returnIngredients.addAll(this.ingredients);
		return returnIngredients;
	}

	public void deleteAllIngredients() {
		this.ingredients.clear();
	}

	public void addTag(final FormulaTag tag) {
		this.tags.add(tag);
	}

	public void addAllTags(final Iterable<FormulaTag> tagList) {
		for (FormulaTag tag : tagList) {
			this.tags.add(tag);
		}
	}

	public Iterator<FormulaTag> getTags() {
		return this.tags.iterator();
	}

	public void deleteAllTags() {
		this.tags.clear();
	}

	public Set<String> getTagsAsStrings() {
		final Set<String> retval = new HashSet<>(this.tags.size());
		for (final FormulaTag tag : this.tags) {
			retval.add(tag.getName());
		}
		return retval;
	}

	@Override
	public String toString() {
		final Set<String> tagList = getTagsAsStrings();
		final String joinedTags = String.join(",", tagList);
		final StringBuilder builder = new StringBuilder();
		builder.append(this.name).append(" | ").append(this.description).append(" | [").append(joinedTags).append(']');

		return builder.toString();

	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj != null && this.getClass() == obj.getClass()) {
			final Formula testForm = (Formula) obj;
			return areSimpleMembersEqual(testForm)
							&& areTagsAndIngredientsEquals(testForm);
		}
		return false;
	}

	private boolean areSimpleMembersEqual(Formula testForm) {
		return this.name.equals(testForm.name)
						&& this.totalAmount.equals(testForm.totalAmount)
						&& this.description.equals(testForm.description)
						&& this.notes.equals(testForm.notes);
	}

	private boolean areTagsAndIngredientsEquals(Formula testForm) {
		return this.tags.equals(testForm.tags)
						&& this.ingredients.equals(testForm.ingredients);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
						append(this.name).
						append(this.description).
						append(this.notes).
						append(this.totalAmount).
						append(this.tags).
						append(this.ingredients).
						toHashCode();
	}

}
