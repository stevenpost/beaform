package beaform.domain;

public class IngredientImpl implements Ingredient {

	private final Formula formula;
	private final String amount;

	public IngredientImpl(Formula formula, String amount) {
		this.formula = formula;
		this.amount = amount;
	}

	@Override
	public String getName() {
		return this.formula.getName();
	}

	@Override
	public String getAmount() {
		return this.amount;
	}

}
