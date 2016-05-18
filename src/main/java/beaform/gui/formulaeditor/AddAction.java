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

import beaform.entities.FormulaDAO;
import beaform.entities.FormulaTag;
import beaform.entities.Ingredient;

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
	private final JTextField txtNameField;

	/** a text field for the description of the new formula */
	private final JTextField txtDescription;

	/** a text field for the total amount in the formula */
	private final JTextField txtTotalAmount;

	/** a reference to the tag panel */
	private final TagPane tagPane;

	/** a reference to the ingredients panel */
	private final IngredientPane ingredientPane;

	/**
	 * Constructor
	 * @param txtNameField text field for the name of the new formula
	 * @param txtDescription text field for the description of the new formula
	 * @param txtTotalAmount text field for the total amount in the formula
	 * @param ingredientPane a reference to the ingredients panel
	 * @param tagPane reference to the tag panel
	 */
	public AddAction(final JTextField txtNameField, final JTextField txtDescription, final JTextField txtTotalAmount, final IngredientPane ingredientPane, final TagPane tagPane) {
		this.txtNameField = txtNameField;
		this.txtDescription = txtDescription;
		this.txtTotalAmount = txtTotalAmount;
		this.ingredientPane = ingredientPane;
		this.tagPane = tagPane;
	}

	/**
	 * Invoked when the action occurs.
	 *
	 * @param event the event object
	 */
	@Override
	public void actionPerformed(final ActionEvent event) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Add: " + this.txtNameField.getText() + " with description: " + this.txtDescription.getText());
		}

		// Get ingredients in a list
		@SuppressWarnings("unchecked")
		final List<Ingredient> ingredients = IteratorUtils.toList(this.ingredientPane.getIngredients());

		// Get tags in a list
		@SuppressWarnings("unchecked")
		final List<FormulaTag> tags = IteratorUtils.toList(this.tagPane.getTags());

		try {
			new FormulaDAO().addFormula(this.txtNameField.getText(), this.txtDescription.getText(), this.txtTotalAmount.getText(), ingredients, tags);
		}
		catch (SystemException | NotSupportedException e1) {
			LOG.error("Something wen wrong adding the new formula", e1);
		}

	}

}
