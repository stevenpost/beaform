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

import beaform.entities.Formula;
import beaform.entities.FormulaDAO;
import beaform.entities.FormulaTag;
import beaform.entities.Ingredient;

/**
 * This class kicks off an update to a existing formula.
 * @author steven
 *
 */
public class SaveExistingAction implements ActionListener {

	/**
	 * The logger.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SaveExistingAction.class);

	/** the formula that gets editing */
	private final transient Formula formula;

	/** a text field for the description of the new formula */
	private final transient JTextField txtDescription;

	/** a text field for the total amount in the formula */
	private final transient JTextField txtTotalAmount;

	/** a reference to the tag panel */
	private final transient TagPane tagPane;

	/** a reference to the ingredients panel */
	private final transient IngredientPane ingredientPane;

	/**
	 * Constructor
	 *
	 * @param formula the formula that needs editing
	 * @param txtDescription text field for the description of the new formula
	 * @param txtTotalAmount text field for the total amount in the formula
	 * @param ingredientPane a reference to the ingredients panel
	 * @param tagPane reference to the tag panel
	 */
	public SaveExistingAction(final Formula formula, final JTextField txtDescription, final JTextField txtTotalAmount, final IngredientPane ingredientPane, final TagPane tagPane) {
		this.formula = formula;
		this.txtDescription = txtDescription;
		this.txtTotalAmount = txtTotalAmount;
		this.ingredientPane = ingredientPane;
		this.tagPane = tagPane;
	}

	/**
	 * This method is invoked when the event is triggered.
	 *
	 * @param event The event object
	 */
	@Override
	public void actionPerformed(final ActionEvent event) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Edit: " + this.formula.getName() + " with description: " + this.txtDescription.getText());
		}

		// Get ingredients in a list
		@SuppressWarnings("unchecked")
		final List<Ingredient> ingredients = IteratorUtils.toList(this.ingredientPane.getIngredients());

		// Get tags in a list
		@SuppressWarnings("unchecked")
		final List<FormulaTag> tags = IteratorUtils.toList(this.tagPane.getTags());
		try {
			new FormulaDAO().updateExisting(this.formula.getName(), this.txtDescription.getText(), this.txtTotalAmount.getText(), ingredients, tags);
		}
		catch (SystemException | NotSupportedException e1) {
			LOG.error("Something went wrong updating the formula", e1);
		}

	}

}
