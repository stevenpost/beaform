package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.dao.InvalidFormulaException;
import beaform.dao.NoSuchFormulaException;

public class UpdateFormulaAction implements ActionListener {

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
			this.editor.showErrorMessage(ex.getMessage());
		}
	}

}
