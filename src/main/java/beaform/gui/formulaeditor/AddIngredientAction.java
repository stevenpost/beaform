package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JTextField;

import beaform.entities.Formula;

public final class AddIngredientAction implements ActionListener {

	private final JTextField txtNewIngredient;
	private final DefaultListModel<Formula> lstFormulaModel;

	public AddIngredientAction(JTextField txtNewIngredient, DefaultListModel<Formula> lstFormulaModel) {
		this.txtNewIngredient = txtNewIngredient;
		this.lstFormulaModel = lstFormulaModel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String strIngredient = this.txtNewIngredient.getText();
		if (!"".equals(strIngredient)) {
			this.txtNewIngredient.setText("");
			Formula form = new Formula();
			form.setName(strIngredient);
			this.lstFormulaModel.addElement(form);
		}
	}
}