package beaform.entities;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This class represents an ingredient
 *
 * @author steven
 *
 */
public class Ingredient {

	/**
	 * The formula of this ingredient
	 */
	private final Formula formula;
	/**
	 * The amount of this ingredient
	 */
	private final String amount;

	/**
	 * Constructor
	 * @param formula The formula of this ingredient
	 * @param amount the amount of this ingredient
	 */
	public Ingredient(final Formula formula, final String amount) {
		this.formula = formula;
		this.amount = amount;
	}

	/**
	 * Gets the formula
	 * @return the formula
	 */
	public Formula getFormula() {
		return this.formula;
	}

	/**
	 * Gets the amount
	 * @return the amount
	 */
	public String getAmount() {
		return this.amount;
	}

	/**
	 * Return a string representation of this object.
	 * This String is formed by its name, followed by the amount enclosed in brackets.
	 *
	 * @return the string representation
	 */
	@Override
	public String toString() {
		return this.formula.getName() + " [" + this.amount + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj != null && this.getClass() == obj.getClass()) {
			final Ingredient testIngredient = (Ingredient) obj;
			return this.amount.equals(testIngredient.amount)
							&& this.formula.equals(testIngredient.formula);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().
						append(this.amount).
						append(this.formula).
						toHashCode();
	}

}
