package beaform.gui.formulaeditor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.Ingredient;
import beaform.entities.Formula;
import beaform.entities.FormulaDAO;
import beaform.entities.FormulaTag;

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

	/** Dimensions for the amount field */
	private static final Dimension DIM_AMOUNT = new Dimension(60, 30);

	/** Dimensions for lists */
	private static final Dimension DIM_LISTS = new Dimension(200, 100);

	/** A label for the total amount of a formula */
	private static final JLabel LBL_TOTAL_AMOUNT = new JLabel("total amount");

	/** A label for the name of a formula */
	private static final JLabel LBL_NAME = new JLabel("Name");

	/** A label for the description of a formula */
	private static final JLabel LBL_DESCRIPTION = new JLabel("Description");

	/** A label for the list of ingredients */
	private static final JLabel LBL_INGREDIENTS = new JLabel("Ingredients");

	/** A label for the name of ingredients */
	private static final JLabel LBL_INGREDIENT_NAME = new JLabel("Name");

	/** A label for the amount in an ingredient */
	private static final JLabel LBL_AMOUNT = new JLabel("Amount");

	/** A text field for the name of the formula */
	private final transient JTextField txtName = new JTextField();

	/** A text field for the description of a formula */
	private final transient JTextField txtDescription = new JTextField();

	/** A text field for the total amount of a formula */
	private final transient JTextField txtTotalAmount = new JTextField();

	/** A list model to get the list of formulas to the screen */
	private final transient DefaultListModel<Ingredient> lstFormulaModel = new DefaultListModel<Ingredient>();

	/** The 'save' button */
	private final transient JButton btnSubmit = new JButton("Submit");

	/** The panel with all the tag components */
	private final transient TagPane tagPane = new TagPane();

	/**
	 * Main constructor for this editor to add a new formula.
	 * If you want to edit an existing one,
	 * use the overridden constructor that takes a formula as argument.
	 */
	public FormulaEditor() {
		super(new GridBagLayout());
		init(true);
		this.btnSubmit.addActionListener(new AddAction(this.txtName, this.txtDescription, this.txtTotalAmount, this.lstFormulaModel, this.tagPane));
	}

	/**
	 * Constructor that makes this an editor for existing formulas.
	 *
	 * @param formula The formula that needs editing.
	 */
	public FormulaEditor(final Formula formula) {
		super(new GridBagLayout());
		init(false);

		this.txtName.setText(formula.getName());
		this.txtDescription.setText(formula.getDescription());
		this.txtTotalAmount.setText(formula.getTotalAmount());

		try {
			// Add ingredients to the list
			final List<Ingredient> ingredientList = new FormulaDAO().getIngredients(formula);
			for (final Ingredient ingredient : ingredientList) {
				this.lstFormulaModel.addElement(ingredient);
			}

			// Add tags to the list
			final Iterator<FormulaTag> tagIterator = formula.getTags();
			this.tagPane.addMultipleTags(tagIterator);
		}
		catch (NotSupportedException | SystemException e) {
			LOG.error("Failed to add all tags and ingredients", e);
		}

		this.btnSubmit.addActionListener(new SaveExistingAction(formula, this.txtDescription, this.txtTotalAmount, this.lstFormulaModel, this.tagPane));

	}

	private void init(final boolean isNew) {
		int gridy = 0;

		// Formula requirements
		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = gridy;
		this.add(LBL_NAME, constraints);

		constraints.gridx = 1;
		constraints.gridy = gridy;
		setDimensions(this.txtName, DIM_TEXTFIELDS);
		this.txtName.setEnabled(isNew);
		this.add(this.txtName, constraints);

		gridy++;
		constraints.gridx = 0;
		constraints.gridy = gridy;
		this.add(LBL_DESCRIPTION, constraints);

		constraints.gridx = 1;
		constraints.gridy = gridy;
		setDimensions(this.txtDescription, DIM_TEXTFIELDS);
		this.add(this.txtDescription, constraints);

		gridy++;
		constraints.gridx = 0;
		constraints.gridy = gridy;
		setDimensions(LBL_TOTAL_AMOUNT, DIM_TEXTFIELDS);
		this.add(LBL_TOTAL_AMOUNT, constraints);

		constraints.gridx = 1;
		constraints.gridy = gridy;
		setDimensions(this.txtTotalAmount, DIM_TEXTFIELDS);
		this.add(this.txtTotalAmount, constraints);

		// Ingredients
		gridy++;
		gridy = addIngredientComponents(gridy, constraints);

		// Tags
		gridy++;
		constraints.gridx = 0;
		constraints.gridy = gridy;
		constraints.gridwidth = 3;
		constraints.gridheight = 4;
		this.add(this.tagPane, constraints);
		constraints.gridheight = 1;
		constraints.gridwidth = 1;

		// Submit
		gridy++;
		constraints.gridx = 0;
		constraints.gridy = gridy;
		constraints.gridwidth = 2;
		this.add(this.btnSubmit, constraints);
	}

	/**
	 * This method adds the ingredient components to the GUI
	 *
	 * @param tmpGridy the y value for the grid
	 * @param constraints the constraints to work with
	 * @return the y value of the last item
	 */
	private int addIngredientComponents(final int gridy, final GridBagConstraints constraints) {

		int tmpGridy = gridy;
		final JList<Ingredient> lstFormulas = new JList<Ingredient>(this.lstFormulaModel);
		final JTextField txtNewIngredient = new JTextField();
		final JTextField txtNewIngredientAmount = new JTextField();
		final JButton btnAddIngredient = new JButton("Add ingedrient");
		final JButton btnDelIngredient = new JButton("Del ingedrient");

		constraints.gridx = 0;
		constraints.gridy = tmpGridy;
		constraints.gridwidth = 2;
		this.add(LBL_INGREDIENTS, constraints);
		constraints.gridwidth = 1;

		tmpGridy++;
		constraints.gridx = 0;
		constraints.gridy = tmpGridy;
		constraints.gridheight = 4;
		setDimensions(lstFormulas, DIM_LISTS);
		this.add(lstFormulas, constraints);
		constraints.gridheight = 1;

		constraints.gridx = 1;
		constraints.gridy = tmpGridy;
		setDimensions(LBL_INGREDIENT_NAME, DIM_TEXTFIELDS);
		this.add(LBL_INGREDIENT_NAME, constraints);

		constraints.gridx = 2;
		constraints.gridy = tmpGridy;
		setDimensions(LBL_AMOUNT, DIM_AMOUNT);
		this.add(LBL_AMOUNT, constraints);

		tmpGridy++;
		constraints.gridx = 1;
		constraints.gridy = tmpGridy;
		setDimensions(txtNewIngredient, DIM_TEXTFIELDS);
		this.add(txtNewIngredient, constraints);

		constraints.gridx = 2;
		constraints.gridy = tmpGridy;
		setDimensions(txtNewIngredientAmount, DIM_AMOUNT);
		this.add(txtNewIngredientAmount, constraints);

		tmpGridy++;
		constraints.gridx = 1;
		constraints.gridy = tmpGridy;
		btnAddIngredient.addActionListener(new AddIngredientAction(txtNewIngredient, txtNewIngredientAmount, this.lstFormulaModel));
		this.add(btnAddIngredient, constraints);

		tmpGridy++;
		constraints.gridx = 1;
		constraints.gridy = tmpGridy;
		btnDelIngredient.addActionListener(new DelIngredientAction(lstFormulas, this.lstFormulaModel));
		this.add(btnDelIngredient, constraints);

		return tmpGridy;
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

}
