package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.NoSuchFormulaException;
import beaform.entities.InvalidFormulaException;
import beaform.gui.main.ErrorDisplay;

public class UpdateFormulaAction implements ActionListener {

	private static final Logger LOG = LoggerFactory.getLogger(UpdateFormulaAction.class);
	private final FormulaEditor editor;
	private final ErrorDisplay errorDisplay;

	public UpdateFormulaAction(final FormulaEditor editor, final ErrorDisplay errorDisplay) {
		this.editor = editor;
		this.errorDisplay = errorDisplay;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			this.editor.updateFormula();
		}
		catch (NoSuchFormulaException | InvalidFormulaException ex) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("The formula is not valid or doesn't exist", ex);
			}
			this.errorDisplay.displayError(ex.getMessage());
		}
	}

}
