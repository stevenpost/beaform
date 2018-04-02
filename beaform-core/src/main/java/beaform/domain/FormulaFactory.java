package beaform.domain;

public class FormulaFactory {

	public static Formula createFormula(String name, String description) {
		if (name == null || "".equals(name)) {
			throw new InvalidNameException();
		}
		return new FormulaImpl(name, description);
	}

}
