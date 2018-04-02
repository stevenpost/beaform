package beaform.commands;

import beaform.dao.FormulaDAO;
import beaform.entities.Formula;

public class UpdateFormulaCommand implements Command {

	private final Formula formula;

	public UpdateFormulaCommand(Formula formula) {
		this.formula = formula;
	}

	@Override
	public void execute() {
		FormulaDAO.updateExistingInDb(this.formula);
	}

}
