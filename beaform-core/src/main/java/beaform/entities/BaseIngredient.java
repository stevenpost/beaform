package beaform.entities;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class BaseIngredient implements Ingredient {

	private final BaseCompound base;
	private final String amount;

	public BaseIngredient(final BaseCompound base, final String amount) {
		this.base = base;
		this.amount = amount;
	}

	@Override
	public String getAmount() {
		return this.amount;
	}

	public BaseCompound getBase() {
		return this.base;
	}

	public String getName() {
		return this.base.getName();
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj != null && this.getClass() == obj.getClass()) {
			final BaseIngredient testIngredient = (BaseIngredient) obj;
			return this.base.equals(testIngredient.base) && this.amount.equals(testIngredient.amount);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
						append(this.base).
						append(this.amount).
						toHashCode();
	}

}
