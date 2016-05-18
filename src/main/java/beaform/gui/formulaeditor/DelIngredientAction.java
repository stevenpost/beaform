package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import beaform.entities.Ingredient;

public final class DelIngredientAction implements ActionListener {

	private final DefaultListModel<Ingredient> lstFormulaModel;
	private final JList<Ingredient> lstFormulas;

	public DelIngredientAction(JList<Ingredient> lstFormulas, DefaultListModel<Ingredient> lstFormulaModel) {
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