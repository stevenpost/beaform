package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.ListModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.Ingredient;
import beaform.entities.Formula;
import beaform.entities.FormulaDAO;
import beaform.entities.Tag;

public class SaveExistingAction implements ActionListener {

	private static final Logger LOG = LoggerFactory.getLogger(SaveExistingAction.class);

	private final JTextField txtDescriptionField;
	private final JTextField txtTotalAmount;
	private final List<Tag> tags;
	private final ListModel<Ingredient> lstIngredients;
	private final Formula formula;

	public SaveExistingAction(Formula formula, JTextField txtDescriptionField, JTextField txtTotalAmount, ListModel<Ingredient> lstFormulas, List<Tag> tags) {
		this.formula = formula;
		this.txtDescriptionField = txtDescriptionField;
		this.txtTotalAmount = txtTotalAmount;
		this.lstIngredients = lstFormulas;
		this.tags = tags;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LOG.info("Add: " + this.formula.getName() + " with description: " + this.txtDescriptionField.getText());

		// Get Ingredients in a list
		final List<Ingredient> ingredients = new ArrayList<Ingredient>();
		final int nrOfIngredients = this.lstIngredients.getSize();
		for (int i = 0; i < nrOfIngredients; i++) {
			// See if the tag exist in the DB, if so, use it.
			ingredients.add(this.lstIngredients.getElementAt(i));
		}

		new FormulaDAO().updateExisting(this.formula.getName(), this.txtDescriptionField.getText(), this.txtTotalAmount.getText(), ingredients, this.tags);

	}

}
