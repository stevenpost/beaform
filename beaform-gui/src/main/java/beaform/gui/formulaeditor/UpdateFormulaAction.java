package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.NoSuchFormulaException;
import beaform.entities.InvalidFormulaException;

public class UpdateFormulaAction implements ActionListener {

	private static final Logger LOG = LoggerFactory.getLogger(UpdateFormulaAction.class);
	private final FormulaEditor editor;

	public UpdateFormulaAction(final FormulaEditor editor) {
		this.editor = editor;
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
			this.editor.showErrorMessage(ex.getMessage());
		}
	}

}
