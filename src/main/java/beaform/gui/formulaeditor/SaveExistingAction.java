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

	private final JTextField txtNameField;
	private final JTextField txtDescriptionField;
	private final ListModel<Tag> lstTags;
	private final ListModel<Ingredient> lstIngredients;
	private final Formula formula;

	public SaveExistingAction(Formula formula, JTextField txtNameField, JTextField txtDescriptionField, ListModel<Ingredient> lstFormulas, ListModel<Tag> lstTags) {
		this.formula = formula;
		this.txtNameField = txtNameField;
		this.txtDescriptionField = txtDescriptionField;
		this.lstIngredients = lstFormulas;
		this.lstTags = lstTags;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LOG.info("Add: " + this.txtNameField.getText() + " with description: " + this.txtDescriptionField.getText());

		// Get tags in a list
		List<Tag> tags = new ArrayList<Tag>();
		int nrOfTags = this.lstTags.getSize();
		for (int i = 0; i < nrOfTags; i++) {
			// See if the tag exist in the DB, if so, use it.
			tags.add(this.lstTags.getElementAt(i));
		}

		// Get Ingredients in a list
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		int nrOfIngredients = this.lstIngredients.getSize();
		for (int i = 0; i < nrOfIngredients; i++) {
			// See if the tag exist in the DB, if so, use it.
			ingredients.add(this.lstIngredients.getElementAt(i));
		}

		new FormulaDAO().updateExisting(this.formula.getName(), this.txtNameField.getText(), this.txtDescriptionField.getText(), ingredients, tags);

	}

}
