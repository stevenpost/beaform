package beaform.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import beaform.gui.formulaeditor.FormulaEditor;

public class NewAddWindowAction implements ActionListener {

	private final JPanel panel;

	public NewAddWindowAction(JPanel panel) {
		this.panel = panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.panel.add(new FormulaEditor());
		this.panel.revalidate();
	}

}
