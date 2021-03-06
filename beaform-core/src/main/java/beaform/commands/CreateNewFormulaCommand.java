package beaform.commands;

import org.neo4j.graphdb.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.FormulaDAO;
import beaform.entities.Formula;
import beaform.utilities.ErrorDisplay;

public class CreateNewFormulaCommand implements Command {

	private static final Logger LOG = LoggerFactory.getLogger(CreateNewFormulaCommand.class);
	private final Formula formula;
	private final ErrorDisplay errorDisplay;

	public CreateNewFormulaCommand(Formula formula, ErrorDisplay errorDisplay) {
		this.formula = formula;
		this.errorDisplay = errorDisplay;
	}

	@Override
	public void execute() {
		try {
			FormulaDAO.addFormula(this.formula);
		}
		catch (ConstraintViolationException cve) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("The formula already exists", cve);
			}
			displayError(cve);
		}
	}

	private void displayError(Exception e) {
		String formulaName = this.formula.getName();
		String errorMessageFormat = "A formula with the name %s already seems to exist:%n%s";
		String errorMessage = String.format(errorMessageFormat, formulaName, e.getMessage());
		this.errorDisplay.displayError(errorMessage);
	}

}
