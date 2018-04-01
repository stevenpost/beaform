package beaform.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.FormulaDAO;
import beaform.entities.Formula;
import beaform.events.Event;
import beaform.events.FormulaCreatedEvent;

public class CreateNewFormulaCommand implements Command {
	private static final Logger LOG = LoggerFactory.getLogger(CreateNewFormulaCommand.class);

	private final Formula formula;

	public CreateNewFormulaCommand(Formula formula) {
		this.formula = formula;
	}

	@Override
	public void execute() {
		Event create = new FormulaCreatedEvent(this.formula.getName());
		LOG.debug(create.toEventString());
		FormulaDAO.addFormula(this.formula);
	}

}
