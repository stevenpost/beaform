package beaform.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.FormulaDAO;
import beaform.entities.Formula;
import beaform.entities.InvalidFormulaException;
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
		catch (InvalidFormulaException ife) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("The formula is not valid", ife);
			}
			this.errorDisplay.displayError(ife.getMessage());
		}
	}

}
