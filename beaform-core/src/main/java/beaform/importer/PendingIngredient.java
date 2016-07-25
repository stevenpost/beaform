package beaform.importer;

public class PendingIngredient {

	private final String name;
	private final String amount;

	public PendingIngredient(final String name, final String amount) {
		this.name = name;
		this.amount = amount;
	}

	public String getName() {
		return this.name;
	}

	public String getAmount() {
		return this.amount;
	}

}
