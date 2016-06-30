package beaform.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * An aroma formula, the core of the whole application.
 *
 * @author Steven Post
 *
 */
@Entity
public class Formula {

	@Id
	private String name = "";
	private String description = "";
	private String totalAmount = "";

	@OneToMany
	private Map<String, Formula> ingredients = new ConcurrentHashMap<>();

	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<FormulaTag> tags = new ArrayList<>();

	public Formula() {
		// Default constructor for Hibernate.
	}

	public Formula(final String name, final String description, final String totalAmount) {
		this.name = name;
		this.description = description;
		this.totalAmount = totalAmount;
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

	public void addIngredient(final Formula ingredient, final String amount) {
		this.ingredients.put(ingredient.getName() + "|" + amount, ingredient);
	}

	public List<Ingredient> getIngredients() {
		final ArrayList<Ingredient> returnIngredients = new ArrayList<>();

		for (final Entry<String, Formula> entry : this.ingredients.entrySet()) {
			final Ingredient ingredient = createIngredient(entry.getKey(), entry.getValue());
			returnIngredients.add(ingredient);
		}

		return returnIngredients;
	}

	private static Ingredient createIngredient(final String amount, final Formula formula) {
		final String tmpAmount = amount.substring(amount.indexOf('|') + 1);

		return new Ingredient(formula, tmpAmount);
	}

	public void clearIngredients() {
		this.ingredients.clear();
	}

	public void addTag(final FormulaTag tag) {
		this.tags.add(tag);
	}

	public Iterator<FormulaTag> getTags() {
		return this.tags.iterator();
	}

	public void clearTags() {
		this.tags.clear();
	}

	public List<String> getTagsAsStrings() {
		final List<String> retval = new ArrayList<>(this.tags.size());
		for (final FormulaTag tag : this.tags) {
			retval.add(tag.getName());
		}
		return retval;
	}

	@Override
	public String toString() {
		final List<String> tagList = getTagsAsStrings();
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
			return this.name.equals(testForm.name)
							&& this.description.equals(testForm.description)
							&& this.totalAmount.equals(testForm.totalAmount)
							&& this.tags.equals(testForm.tags)
							&& this.ingredients.equals(testForm.ingredients);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
						append(this.name).
						append(this.description).
						append(this.totalAmount).
						append(this.tags).
						append(this.ingredients).
						toHashCode();
	}

}
