package beaform.entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Formula {

	@Id @GeneratedValue(generator = "uuid")
	@GenericGenerator(name="uuid", strategy="uuid2")

	private String id;
	private String name;
	private String description;

	@OneToMany
	private final Map<String, Formula> ingredients = new HashMap<String, Formula>();

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
		builder.append(this.id);
		builder.append(" | ");
		builder.append(this.name);
		builder.append(" | ");
		builder.append(this.description);

		return builder.toString();

	}

}
