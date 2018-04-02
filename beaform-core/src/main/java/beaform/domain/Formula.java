package beaform.domain;

import java.util.Set;

public interface Formula {

	String getName();
	String getDescription();
	Set<Ingredient> getIngredients();
	void addIngredient(Formula ingr1, String amount);

}
