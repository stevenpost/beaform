package beaform.entities;

public class BaseIngredient implements Ingredient {

	private final String name;
	private final String amount;

	public BaseIngredient(final String name, final String amount) {
		this.name = name;
		this.amount = amount;
	}

	@Override
	public String getAmount() {
		return this.amount;
	}

	public String getName() {
		return this.name;
	}

}
