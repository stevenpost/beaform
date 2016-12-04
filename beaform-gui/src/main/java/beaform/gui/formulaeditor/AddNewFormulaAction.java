package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.dao.InvalidFormulaException;

public class AddNewFormulaAction implements ActionListener {

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
			this.editor.showErrorMessage(ife.getMessage());
		}
	}

}
