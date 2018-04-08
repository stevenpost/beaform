package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddNewFormulaAction implements ActionListener {

	private final FormulaEditor editor;

	public AddNewFormulaAction(final FormulaEditor editor) {
		this.editor = editor;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.editor.addNewFormula();
	}

}
