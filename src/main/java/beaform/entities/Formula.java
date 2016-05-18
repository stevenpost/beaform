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

@Entity
public class Formula {

	@Id
	private String name;
	private String description;
	private String totalAmount;

	@OneToMany
	private final Map<String, Formula> ingredients = new ConcurrentHashMap<String, Formula>();

	@OneToMany(fetch=FetchType.EAGER)
	private final List<FormulaTag> tags = new ArrayList<FormulaTag>();

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
		final ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

		for (final Entry<String, Formula> entry : this.ingredients.entrySet()) {
			final Formula formula = entry.getValue();
			String amount = entry.getKey();
			amount = amount.substring(amount.indexOf('|') + 1);
			ingredients.add(new Ingredient(formula, amount)); // NOPMD by steven on 5/17/16 11:43 PM
		}

		return ingredients;
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
		final List<String> retval = new ArrayList<String>(this.tags.size());
		for (final FormulaTag tag : this.tags) {
			retval.add(tag.getName());
		}
		return retval;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(this.name).append(" | ").append(this.description).append(" | [").append(String.join(",", this.getTagsAsStrings())).append(']');

		return builder.toString();

	}

}
