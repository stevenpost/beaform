package beaform.gui.formulaeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
 * This class represents a GUI for editing formulas.
 *
 * @author Steven Post
 *
 */
public class FormulaEditor extends JPanel {

	/** A serial */
	private static final long serialVersionUID = 2557014310487638917L;

	/** A logger */
	private static final Logger LOG = LoggerFactory.getLogger(FormulaEditor.class);

	/** Dimensions for most text fields */
	private static final Dimension DIM_TEXTFIELDS = new Dimension(100, 30);

	/** Dimensions for the description field */
	private static final Dimension DIM_TEXTAREA = new Dimension(100, 90);

	/** A label for the total amount of a formula */
	private static final JLabel LBL_TOTAL_AMOUNT = new JLabel("total amount", SwingConstants.RIGHT);

	/** A label for the name of a formula */
	private static final JLabel LBL_NAME = new JLabel("Name", SwingConstants.RIGHT);

	/** A label for the description of a formula */
	private static final JLabel LBL_DESCRIPTION = new JLabel("Description", SwingConstants.RIGHT);

	/** A text field for the name of the formula */
	private final JTextField txtName = new JTextField();

	/** A text field for the description of a formula */
	private final JTextArea txtDescription = new JTextArea();

	/** A text field for the total amount of a formula */
	private final JTextField txtTotalAmount = new JTextField();

	/** The 'save' button */
	private final JButton btnSubmit = new JButton("Submit");

	/** The panel with all the tag components */
	private final TagPane tagPane = new TagPane();

	/** The panel with all the ingredient components */
	private final IngredientPane ingredientPane = new IngredientPane();

	/** The formula that needs editing */
	private Formula formula;

	/** A panel for the general components */
	private final JPanel panel = new JPanel(new GridBagLayout());

	/**
	 * Main constructor for this editor to add a new formula.
	 * If you want to edit an existing one,
	 * use the overridden constructor that takes a formula as argument.
	 */
	public FormulaEditor() {
		super();
		init();

		this.btnSubmit.addActionListener(new ActionListener() {

			/**
			 * Invoked when the button is pressed.
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				addNewFormula();
			}
		});
	}

	/**
	 * Constructor that makes this an editor for existing formulas.
	 *
	 * @param formula The formula that needs editing.
	 */
	public FormulaEditor(final Formula formula) {
		super();
		init();
		this.txtName.setEnabled(false);

		this.formula = formula;

		this.txtName.setText(formula.getName());
		this.txtDescription.setText(formula.getDescription());
		this.txtTotalAmount.setText(formula.getTotalAmount());

		try {
			// Add ingredients to the list
			final List<Ingredient> ingredientList = new FormulaDAO().getIngredients(formula);
			for (final Ingredient ingredient : ingredientList) {
				this.ingredientPane.addIngredient(ingredient);
			}

			// Add tags to the list
			final Iterator<FormulaTag> tagIterator = formula.getTags();
			this.tagPane.addMultipleTags(tagIterator);
		}
		catch (NotSupportedException | SystemException e) {
			LOG.error("Failed to add all tags and ingredients", e);
		}

		this.btnSubmit.addActionListener(new ActionListener() {

			/**
			 * Invoked when the button is pressed.
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				updateFormula();
			}
		});

	}

	private void init() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		final JPanel generalPanel = createGeneralComponentsPanel();
		this.add(generalPanel);

		// Ingredients
		this.add(this.ingredientPane);

		// Tags
		this.add(this.tagPane);

		// Submit
		this.add(this.btnSubmit);
	}

	private JPanel createGeneralComponentsPanel() {
		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 0.1;
		constraints.weighty = 0.1;
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		// Formula requirements
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.panel.add(LBL_NAME, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		setDimensions(this.txtName, DIM_TEXTFIELDS);
		this.panel.add(this.txtName, constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;
		this.panel.add(LBL_DESCRIPTION, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		setDimensions(LBL_TOTAL_AMOUNT, DIM_TEXTFIELDS);
		this.panel.add(LBL_TOTAL_AMOUNT, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		setDimensions(this.txtTotalAmount, DIM_TEXTFIELDS);
		this.panel.add(this.txtTotalAmount, constraints);

		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.gridheight = 2;
		constraints.fill = GridBagConstraints.BOTH;
		this.txtDescription.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setDimensions(this.txtDescription, DIM_TEXTAREA);
		this.panel.add(this.txtDescription, constraints);

		return this.panel;
	}

	/**
	 * This method sets 3 different sizes to a component:
	 * minimum, preferred and maximum
	 *
	 * @param comp The component
	 * @param dim The dimensions for the sizing
	 */
	private void setDimensions(final JComponent comp, final Dimension dim) {
		comp.setMinimumSize(dim);
		comp.setPreferredSize(dim);
		comp.setMaximumSize(dim);
	}

	/**
	 * Invoked when the action occurs.
	 *
	 */
	public void addNewFormula() {
		if (LOG.isInfoEnabled()) {
			LOG.info("Add: " + this.txtName.getText() + " with description: " + this.txtDescription.getText());
		}

		final List<Ingredient> ingredients = getIngredientList();
		final List<FormulaTag> tags = getTagList();

		try {
			new FormulaDAO().addFormula(this.txtName.getText(), this.txtDescription.getText(), this.txtTotalAmount.getText(), ingredients, tags);
		}
		catch (SystemException | NotSupportedException e1) {
			LOG.error("Something wen wrong adding the new formula", e1);
		}

	}

	@SuppressWarnings("unchecked")
	private List<Ingredient> getIngredientList() {
		return IteratorUtils.toList(this.ingredientPane.getIngredients());
	}

	@SuppressWarnings("unchecked")
	private List<FormulaTag> getTagList() {
		return IteratorUtils.toList(this.tagPane.getTags());
	}

	/**
	 * Updates an existing formula.
	 */
	public void updateFormula() {
		if (LOG.isInfoEnabled()) {
			LOG.info("Edit: " + this.formula.getName() + " with description: " + this.txtDescription.getText());
		}

		final List<Ingredient> ingredients = getIngredientList();
		final List<FormulaTag> tags = getTagList();

		try {
			new FormulaDAO().updateExisting(this.formula.getName(), this.txtDescription.getText(), this.txtTotalAmount.getText(), ingredients, tags);
		}
		catch (SystemException | NotSupportedException e1) {
			LOG.error("Something went wrong updating the formula", e1);
		}

	}

}
