package beaform.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.gui.formulaeditor.FormulaEditor;

public class NewAddWindowAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		MainGUI.getInstance().replaceWindow(new FormulaEditor());
	}

}
