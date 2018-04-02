package beaform.domain;

public interface Ingredient {

	static Ingredient newIngredient(Formula ingr1, String amount) {
		return new IngredientImpl(ingr1, amount);
	}

	String getName();
	String getAmount();

}
