package beaform.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Formula {

	@Id
	private String name;
	private String description;

	@OneToMany
	private final Map<String, Formula> ingredients = new HashMap<String, Formula>();

	@OneToMany
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

	public void setDescription(String description) {
		this.description = description;
	}

	public void addIngredient(Formula ingredient, String amount) {
		this.ingredients.put(ingredient.getName() + "|" + amount, ingredient);
	}

	public Iterator<Entry<String, Formula>> getIngredients() {
		return this.ingredients.entrySet().iterator();
	}

	public void addTag(Tag tag) {
		this.tags.add(tag);
	}

	public Iterator<Tag> getTags() {
		return this.tags.iterator();
	}

	public List<String> getTagsAsStrings() {
		List<String> retval = new ArrayList<String>(this.tags.size());
		for (Tag tag : this.tags) {
			retval.add(tag.getName());
		}
		return retval;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.name);
		builder.append(" | ");
		builder.append(this.description);
		builder.append(" | ");
		builder.append("[");
		builder.append(String.join(",", this.getTagsAsStrings()));
		builder.append("]");

		return builder.toString();

	}

}
