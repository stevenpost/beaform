package beaform;

import beaform.entities.Formula;

/**
 * This class represents an ingredient
 *
 * @author steven
 *
 */
public class Ingredient {
	private final Formula formula;
	private final String amount;

	public Ingredient(final Formula formula, final String amount) {
		this.formula = formula;
		this.amount = amount;
	}

	public Formula getFormula() {
		return this.formula;
	}

	public String getAmount() {
		return this.amount;
	}

	@Override
	public String toString() {
		return this.formula.getName() + " [" + this.amount + "]";
	}

}
