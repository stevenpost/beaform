package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JTextField;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

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

	/** a text field for the name of the new formula */
	private final transient JTextField txtNameField;

	/** a text field for the description of the new formula */
	private final transient JTextField txtDescriptionField;

	/** a text field for the total amount in the formula */
	private final transient JTextField txtTotalAmount;

	/** a reference to the tag panel */
	private final transient TagPane tagPane;

	/** a reference to the ingredients panel */
	private final transient IngredientPane ingredientPane;

	public AddAction(JTextField txtNameField, JTextField txtDescriptionField,JTextField txtTotalAmount, IngredientPane ingredientPane, TagPane tagPane) {
		this.txtNameField = txtNameField;
		this.txtDescriptionField = txtDescriptionField;
		this.txtTotalAmount = txtTotalAmount;
		this.ingredientPane = ingredientPane;
		this.tagPane = tagPane;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Add: " + this.txtNameField.getText() + " with description: " + this.txtDescriptionField.getText());
		}

		// Get ingredients in a list
		@SuppressWarnings("unchecked")
		final List<Ingredient> ingredients = IteratorUtils.toList(this.ingredientPane.getIngredients());

		// Get tags in a list
		@SuppressWarnings("unchecked")
		final List<FormulaTag> tags = IteratorUtils.toList(this.tagPane.getTags());

		try {
			new FormulaDAO().addFormula(this.txtNameField.getText(), this.txtDescriptionField.getText(), this.txtTotalAmount.getText(), ingredients, tags);
		}
		catch (SystemException | NotSupportedException e1) {
			LOG.error("Something wen wrong adding the new formula", e1);
		}

	}

}
