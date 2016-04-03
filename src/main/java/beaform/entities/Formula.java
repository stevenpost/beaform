package beaform.entities;

import java.util.HashMap;
import java.util.Iterator;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.name);
		builder.append(" | ");
		builder.append(this.description);

		return builder.toString();

	}

}
