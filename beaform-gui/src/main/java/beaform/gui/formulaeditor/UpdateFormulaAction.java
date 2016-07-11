package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateFormulaAction implements ActionListener {

	private final FormulaEditor editor;

	public UpdateFormulaAction(final FormulaEditor editor) {
		this.editor = editor;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.editor.updateFormula();
	}

}
