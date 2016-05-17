package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.ListModel;

import org.apache.commons.collections.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.Ingredient;
import beaform.entities.FormulaDAO;
import beaform.entities.FormulaTag;

/**
 * This class kicks off the creation of a new formula from the editor.
 *
 * @author steven
 *
 */
public class AddAction implements ActionListener {

	/** a logger */
	private static final Logger LOG = LoggerFactory.getLogger(AddAction.class);

	private final JTextField txtNameField;
	private final JTextField txtDescriptionField;
	private final JTextField txtTotalAmount;
	private final TagPane tagPane;
	private final ListModel<Ingredient> lstIngredients;

	public AddAction(JTextField txtNameField, JTextField txtDescriptionField,JTextField txtTotalAmount, ListModel<Ingredient> lstFormulas, TagPane tagPane) {
		this.txtNameField = txtNameField;
		this.txtDescriptionField = txtDescriptionField;
		this.txtTotalAmount = txtTotalAmount;
		this.lstIngredients = lstFormulas;
		this.tagPane = tagPane;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Add: " + this.txtNameField.getText() + " with description: " + this.txtDescriptionField.getText());
		}

		// Get Ingredients in a list
		final List<Ingredient> ingredients = new ArrayList<Ingredient>();
		final int nrOfIngredients = this.lstIngredients.getSize();
		for (int i = 0; i < nrOfIngredients; i++) {
			// See if the tag exist in the DB, if so, use it.
			ingredients.add(this.lstIngredients.getElementAt(i));
		}

		@SuppressWarnings("unchecked")
		final List<FormulaTag> tags = IteratorUtils.toList(this.tagPane.getTags());
		new FormulaDAO().addFormula(this.txtNameField.getText(), this.txtDescriptionField.getText(), this.txtTotalAmount.getText(), ingredients, tags);

	}

}
