package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import beaform.entities.Formula;

public final class DelIngredientAction implements ActionListener {

	private final DefaultListModel<Formula> lstFormulaModel;
	private final JList<Formula> lstFormulas;

	public DelIngredientAction(JList<Formula> lstFormulas, DefaultListModel<Formula> lstFormulaModel) {
		this.lstFormulaModel = lstFormulaModel;
		this.lstFormulas = lstFormulas;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		while (!this.lstFormulas.isSelectionEmpty()) {
			int selected = this.lstFormulas.getSelectedIndex();
			this.lstFormulaModel.remove(selected);
		}
	}
}