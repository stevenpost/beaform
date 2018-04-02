package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.entities.InvalidFormulaException;

public class AddNewFormulaAction implements ActionListener {

	private static final Logger LOG = LoggerFactory.getLogger(AddNewFormulaAction.class);
	private final FormulaEditor editor;

	public AddNewFormulaAction(final FormulaEditor editor) {
		this.editor = editor;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			this.editor.addNewFormula();
		}
		catch (InvalidFormulaException ife) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("The formula is not valid", ife);
			}
			this.editor.showErrorMessage(ife.getMessage());
		}
	}

}
