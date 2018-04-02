package beaform.domain;

import org.junit.Test;

@SuppressWarnings("static-method")
public class BasicFormulaErrorHandling {

	private static final String FORMULA_DESCRIPTION = "Testing description";

	@Test(expected = InvalidNameException.class)
	public void testFormulaCreationEmptyName() {
		FormulaFactory.createFormula("", FORMULA_DESCRIPTION);
	}

	@Test(expected = InvalidNameException.class)
	public void testFormulaCreationNullName() {
		FormulaFactory.createFormula(null, FORMULA_DESCRIPTION);
	}

}
