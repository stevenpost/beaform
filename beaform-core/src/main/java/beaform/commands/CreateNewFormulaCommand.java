package beaform.commands;

import beaform.dao.FormulaDAO;
import beaform.entities.Formula;

public class CreateNewFormulaCommand implements Command {

	private final Formula formula;

	public CreateNewFormulaCommand(Formula formula) {
		this.formula = formula;
	}

	@Override
	public void execute() {
		FormulaDAO.addFormula(this.formula);
	}

}
