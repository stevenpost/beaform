package beaform.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.gui.formulaeditor.FormulaEditor;

public class FormulaEditorLaunchAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		final FormulaEditor editor = new FormulaEditor();
		editor.addObserver(MainGUI.getInstance());
		editor.replace();
	}

}
