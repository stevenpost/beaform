package beaform.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.FormulaDAO;
import beaform.dao.NoSuchFormulaException;
import beaform.entities.Formula;
import beaform.entities.InvalidFormulaException;
import beaform.utilities.ErrorDisplay;

public class UpdateFormulaCommand implements Command {

	private static final Logger LOG = LoggerFactory.getLogger(UpdateFormulaCommand.class);
	private final Formula formula;
	private final ErrorDisplay errorDisplay;

	public UpdateFormulaCommand(Formula formula, ErrorDisplay errorDisplay) {
		this.formula = formula;
		this.errorDisplay = errorDisplay;
	}

	@Override
	public void execute() {
		try {
			FormulaDAO.updateExistingInDb(this.formula);
		}
		catch (NoSuchFormulaException | InvalidFormulaException ex) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("The formula is not valid or doesn't exist", ex);
			}
			this.errorDisplay.displayError(ex.getMessage());
		}
	}

}
