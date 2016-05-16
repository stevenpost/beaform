package beaform.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import beaform.Ingredient;

@Entity
public class Formula {

	@Id
	private String name;
	private String description;
	private String totalAmount;

	@OneToMany
	private final Map<String, Formula> ingredients = new HashMap<String, Formula>();

	@OneToMany(fetch=FetchType.EAGER)
	private final List<Tag> tags = new ArrayList<Tag>();

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
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

		for (Entry<String, Formula> entry : this.ingredients.entrySet()) {
			Formula formula = entry.getValue();
			String amount = entry.getKey();
			amount = amount.substring(amount.indexOf('|') + 1);
			ingredients.add(new Ingredient(formula, amount));
		}

		return ingredients;
	}

	public void clearIngredients() {
		this.ingredients.clear();
	}

	public void addTag(final Tag tag) {
		this.tags.add(tag);
	}

	public Iterator<Tag> getTags() {
		return this.tags.iterator();
	}

	public void clearTags() {
		this.tags.clear();
	}

	public List<String> getTagsAsStrings() {
		final List<String> retval = new ArrayList<String>(this.tags.size());
		for (final Tag tag : this.tags) {
			retval.add(tag.getName());
		}
		return retval;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.name).append(" | ").append(this.description).append(" | [").append(String.join(",", this.getTagsAsStrings())).append(']');

		return builder.toString();

	}

}
