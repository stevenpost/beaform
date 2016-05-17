package beaform.gui.formulaeditor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
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

	/** A label for the list of tags */
	private static final JLabel LBL_TAGS = new JLabel("Tags");

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

	/** A list of formula tags */
	private final transient List<FormulaTag> tags = new ArrayList<FormulaTag>();

	/** A list model to get the list of tags to the screen */
	private final transient DefaultListModel<FormulaTag> lstTagModel = new DefaultListModel<FormulaTag>();

	/** A list model to get the list of formulas to the screen */
	private final transient DefaultListModel<Ingredient> lstFormulaModel = new DefaultListModel<Ingredient>();

	/** The 'save' button */
	private final transient JButton btnSubmit = new JButton("Submit");

	/**
	 * Main constructor for this editor to add a new formula.
	 * If you want to edit an existing one,
	 * use the overridden constructor that takes a formula as argument.
	 */
	public FormulaEditor() {
		super(new GridBagLayout());
		init(true);
		this.btnSubmit.addActionListener(new AddAction(this.txtName, this.txtDescription, this.txtTotalAmount, this.lstFormulaModel, this.tags));
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
			while (tagIterator.hasNext()) {
				this.tags.add(tagIterator.next());
			}
			sortTags();
		}
		catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.btnSubmit.addActionListener(new SaveExistingAction(formula, this.txtDescription, this.txtTotalAmount, this.lstFormulaModel, this.tags));

	}

	private void init(final boolean isNew) {
		int y = 0;

		// Formula requirements
		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = y;
		this.add(LBL_NAME, constraints);

		constraints.gridx = 1;
		constraints.gridy = y;
		setDimensions(this.txtName, DIM_TEXTFIELDS);
		this.txtName.setEnabled(isNew);
		this.add(this.txtName, constraints);

		y++;
		constraints.gridx = 0;
		constraints.gridy = y;
		this.add(LBL_DESCRIPTION, constraints);

		constraints.gridx = 1;
		constraints.gridy = y;
		setDimensions(this.txtDescription, DIM_TEXTFIELDS);
		this.add(this.txtDescription, constraints);

		y++;
		constraints.gridx = 0;
		constraints.gridy = y;
		setDimensions(LBL_TOTAL_AMOUNT, DIM_TEXTFIELDS);
		this.add(LBL_TOTAL_AMOUNT, constraints);

		constraints.gridx = 1;
		constraints.gridy = y;
		setDimensions(this.txtTotalAmount, DIM_TEXTFIELDS);
		this.add(this.txtTotalAmount, constraints);

		// Ingredients
		y++;
		y = addIngredientComponents(y, constraints);

		// Tags
		y++;
		y = addTagComponents(y, constraints);

		// Submit
		y++;
		constraints.gridx = 0;
		constraints.gridy = y;
		constraints.gridwidth = 2;
		this.add(this.btnSubmit, constraints);
	}

	/**
	 * This method adds the GUI components for tags to the GUI
	 * @param y the y value for the grid
	 * @param constraints the constraints to work with
	 * @return the y value of the last item
	 */
	private int addTagComponents(int y, final GridBagConstraints constraints) {
		final JList<FormulaTag> lstTags = new JList<FormulaTag>(this.lstTagModel);
		final JTextField txtNewTag = new JTextField();
		final JButton btnAddTag = new JButton("Add Tag");
		final JButton btnDelTag = new JButton("Remove Tag");

		constraints.gridx = 0;
		constraints.gridy = y;
		constraints.gridwidth = 2;
		this.add(LBL_TAGS, constraints);
		constraints.gridwidth = 1;

		y++;
		constraints.gridx = 0;
		constraints.gridy = y;
		constraints.gridheight = 3;
		setDimensions(lstTags, DIM_LISTS);
		this.add(lstTags, constraints);

		constraints.gridx = 1;
		constraints.gridy = y;
		constraints.gridheight = 1;
		setDimensions(txtNewTag, DIM_TEXTFIELDS);
		this.add(txtNewTag, constraints);

		y++;
		constraints.gridx = 1;
		constraints.gridy = y;
		btnAddTag.addActionListener(new AddTagAction(txtNewTag, this));
		this.add(btnAddTag, constraints);

		y++;
		constraints.gridx = 1;
		constraints.gridy = y;
		btnDelTag.addActionListener(new DelTagAction(lstTags, this));
		this.add(btnDelTag, constraints);
		return y;
	}

	/**
	 * This method adds the ingredient components to the GUI
	 *
	 * @param y the y value for the grid
	 * @param constraints the constraints to work with
	 * @return the y value of the last item
	 */
	private int addIngredientComponents(int y, final GridBagConstraints constraints) {

		final JList<Ingredient> lstFormulas = new JList<Ingredient>(this.lstFormulaModel);
		final JTextField txtNewIngredient = new JTextField();
		final JTextField txtNewIngredientAmount = new JTextField();
		final JButton btnAddIngredient = new JButton("Add ingedrient");
		final JButton btnDelIngredient = new JButton("Del ingedrient");

		constraints.gridx = 0;
		constraints.gridy = y;
		constraints.gridwidth = 2;
		this.add(LBL_INGREDIENTS, constraints);
		constraints.gridwidth = 1;

		y++;
		constraints.gridx = 0;
		constraints.gridy = y;
		constraints.gridheight = 4;
		setDimensions(lstFormulas, DIM_LISTS);
		this.add(lstFormulas, constraints);
		constraints.gridheight = 1;

		constraints.gridx = 1;
		constraints.gridy = y;
		setDimensions(LBL_INGREDIENT_NAME, DIM_TEXTFIELDS);
		this.add(LBL_INGREDIENT_NAME, constraints);

		constraints.gridx = 2;
		constraints.gridy = y;
		setDimensions(LBL_AMOUNT, DIM_AMOUNT);
		this.add(LBL_AMOUNT, constraints);

		y++;
		constraints.gridx = 1;
		constraints.gridy = y;
		setDimensions(txtNewIngredient, DIM_TEXTFIELDS);
		this.add(txtNewIngredient, constraints);

		constraints.gridx = 2;
		constraints.gridy = y;
		setDimensions(txtNewIngredientAmount, DIM_AMOUNT);
		this.add(txtNewIngredientAmount, constraints);

		y++;
		constraints.gridx = 1;
		constraints.gridy = y;
		btnAddIngredient.addActionListener(new AddIngredientAction(txtNewIngredient, txtNewIngredientAmount, this.lstFormulaModel));
		this.add(btnAddIngredient, constraints);

		y++;
		constraints.gridx = 1;
		constraints.gridy = y;
		btnDelIngredient.addActionListener(new DelIngredientAction(lstFormulas, this.lstFormulaModel));
		this.add(btnDelIngredient, constraints);
		return y;
	}

	/**
	 * This method sets 3 different sizes to a component:
	 * - minimum
	 * - preferred
	 * - maximum
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
	 * This method adds a tag to the list of tags for this formula
	 *
	 * @param tag the tag to add
	 */
	public void addTag(final FormulaTag tag) {
		this.tags.add(tag);
		sortTags();
	}

	/**
	 * This method sorts the tags alphabetically in the list.
	 * should the list model and the backing list be out of sync,
	 * this method will synchronize them.
	 */
	public final void sortTags() {
		this.tags.sort(new TagComparator());

		this.lstTagModel.clear();
		for (final FormulaTag tag : this.tags) {
			this.lstTagModel.addElement(tag);
		}
	}

	/**
	 * Remove a tag from the list.
	 * @param i the index of the tag to delete
	 */
	public void removeTag(final int index) {
		this.tags.remove(index);
		this.lstTagModel.remove(index);
	}

}
