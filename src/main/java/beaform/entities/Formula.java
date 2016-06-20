package beaform.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * An aroma formula, the core of the whole application.
 *
 * @author Steven Post
 *
 */
@Entity
public class Formula {

	/** The name of this formula */
	@Id
	private String name;

	/** The description of this formula */
	private String description;

	/** The total amount of this formula */
	private String totalAmount;

	/** All ingredients that make up this formula */
	@OneToMany
	private final Map<String, Formula> ingredients = new ConcurrentHashMap<>();

	/** All tags associated with this formula */
	@OneToMany(fetch=FetchType.EAGER)
	private final List<FormulaTag> tags = new ArrayList<>();

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the totalAmount
	 */
	public String getTotalAmount() {
		return this.totalAmount;
	}

	/**
	 * Set the total amount in this formula.
	 * @param totalAmount
	 */
	public void setTotalAmount(final String totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * Add an ingredient to this formula.
	 * @param ingredient the ingredient to add
	 * @param amount the amount of this ingredient
	 */
	public void addIngredient(final Formula ingredient, final String amount) {
		this.ingredients.put(ingredient.getName() + "|" + amount, ingredient);
	}

	/**
	 * Get a list of ingredients.
	 * @return a list of ingredients
	 */
	public List<Ingredient> getIngredients() {
		final ArrayList<Ingredient> ingredients = new ArrayList<>();

		for (final Entry<String, Formula> entry : this.ingredients.entrySet()) {
			final Ingredient ingredient = createIngredient(entry.getKey(), entry.getValue());
			ingredients.add(ingredient);
		}

		return ingredients;
	}

	private Ingredient createIngredient(final String amount, final Formula formula) {
		final String tmpAmount = amount.substring(amount.indexOf('|') + 1);

		return new Ingredient(formula, tmpAmount);
	}

	/**
	 * Delete all ingredients from this formula.
	 */
	public void clearIngredients() {
		this.ingredients.clear();
	}

	/**
	 * Add a tag to this formula.
	 * @param tag the tag to add
	 */
	public void addTag(final FormulaTag tag) {
		this.tags.add(tag);
	}

	/**
	 * Get all the tags associated with this formula.
	 * @return all the tags associated with this formula
	 */
	public Iterator<FormulaTag> getTags() {
		return this.tags.iterator();
	}

	/**
	 * Delete all tags from this formula.
	 */
	public void clearTags() {
		this.tags.clear();
	}

	/**
	 * @return a list of tags, represented as strings
	 */
	public List<String> getTagsAsStrings() {
		final List<String> retval = new ArrayList<>(this.tags.size());
		for (final FormulaTag tag : this.tags) {
			retval.add(tag.getName());
		}
		return retval;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final List<String> tagList = getTagsAsStrings();
		final String tags = String.join(",", tagList);
		final StringBuilder builder = new StringBuilder();
		builder.append(this.name).append(" | ").append(this.description).append(" | [").append(tags).append(']');

		return builder.toString();

	}

}
