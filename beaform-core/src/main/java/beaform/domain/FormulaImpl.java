package beaform.domain;

import java.util.HashSet;
import java.util.Set;

public class FormulaImpl implements Formula {

	private final String name;
	private final String description;
	private final Set<Ingredient> ingredients = new HashSet<>();

	public FormulaImpl(String name, String description) {
		this.name = name;
		this.description = description;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public Set<Ingredient> getIngredients() {
		return this.ingredients;
	}

	@Override
	public void addIngredient(Formula ingr1, String amount) {
		Ingredient ingredient = Ingredient.newIngredient(ingr1, amount);
		this.ingredients.add(ingredient);
	}

}
