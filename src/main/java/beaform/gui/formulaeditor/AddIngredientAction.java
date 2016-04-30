package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.persistence.NoResultException;
import javax.swing.DefaultListModel;
import javax.swing.JTextField;

import beaform.GraphDbHandlerForJTA;
import beaform.Ingredient;
import beaform.SearchFormulaTask;
import beaform.entities.Formula;

public final class AddIngredientAction implements ActionListener {

	private final JTextField txtIngredient;
	private final JTextField txtAmount;
	private final DefaultListModel<Ingredient> lstFormulaModel;

	public AddIngredientAction(JTextField txtIngredient, JTextField txtAmount, DefaultListModel<Ingredient> lstFormulaModel) {
		this.txtIngredient = txtIngredient;
		this.txtAmount = txtAmount;
		this.lstFormulaModel = lstFormulaModel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String ingredient = this.txtIngredient.getText();
		String amount = this.txtAmount.getText();

		if (!"".equals(ingredient)) {
			this.txtIngredient.setText("");
			this.txtAmount.setText("");
			Future<Formula> formTask = GraphDbHandlerForJTA.addTask(new SearchFormulaTask(ingredient));
			Formula form = null;
			try {
				form = formTask.get();
			}
			catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch (ExecutionException e1) {
				if (!(e1.getCause() instanceof NoResultException)) {
					e1.printStackTrace();
				}
			}

			if (form == null) {
				form = new Formula();
				form.setName(ingredient);
			}

			this.lstFormulaModel.addElement(new Ingredient(form, amount));
		}
	}
}