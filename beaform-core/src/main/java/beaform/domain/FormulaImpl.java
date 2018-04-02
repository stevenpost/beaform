package beaform.domain;

public class FormulaImpl implements Formula {

	private final String name;
	private final String description;

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

}
