package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.persistence.NoResultException;
import javax.swing.DefaultListModel;
import javax.swing.JTextField;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import beaform.Ingredient;
import beaform.entities.Formula;
import beaform.entities.FormulaDAO;

/**
 * This action adds an ingredient to the list.
 *
 * @author Steven Post
 *
 */
public final class AddIngredientAction implements ActionListener {

	/** the name of the ingredient */
	private final transient JTextField txtIngredient;

	/** the amount used in the recipe */
	private final transient JTextField txtAmount;

	/** The list of ingredients */
	private final transient DefaultListModel<Ingredient> lstFormulaModel;

	/**
	 * Constructor.
	 * @param txtIngredient the text field with the name of the ingredient
	 * @param txtAmount the text field with the amount of
	 *        the ingredient used in this recipe
	 * @param lstFormulaModel the list for ingredients
	 */
	public AddIngredientAction(final JTextField txtIngredient, final JTextField txtAmount, final DefaultListModel<Ingredient> lstFormulaModel) {
		this.txtIngredient = txtIngredient;
		this.txtAmount = txtAmount;
		this.lstFormulaModel = lstFormulaModel;
	}

	/**
	 * This method is invoked when the event is triggered.
	 *
	 * @param event The event object
	 */
	@Override
	public void actionPerformed(final ActionEvent event) {

		final String ingredient = this.txtIngredient.getText();
		if ("".equals(ingredient)) {
			throw new UnsupportedOperationException("No formula name entered");
		}

		final String amount = this.txtAmount.getText();
		if ("".equals(amount)) {
			throw new UnsupportedOperationException("No amount entered");
		}

		final FormulaDAO formulaDAO = new FormulaDAO();

		try {
			final Formula form = formulaDAO.findFormulaByName(ingredient);
			this.lstFormulaModel.addElement(new Ingredient(form, amount));
		}
		catch (SystemException | NotSupportedException e1) {
			throw new IllegalStateException("Something went wrong when getting the existing formula", e1);
		}
		catch (NoResultException e) {
			throw new UnsupportedOperationException("The entered formula doesn't exist", e);
		}


		this.txtIngredient.setText("");
		this.txtAmount.setText("");
	}
}