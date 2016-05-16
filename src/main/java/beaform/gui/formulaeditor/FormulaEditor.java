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
import beaform.entities.Tag;

public class FormulaEditor extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 2557014310487638917L;

	private static final Dimension txtFieldDimensions = new Dimension(100, 30);
	private static final Dimension amountDimensions = new Dimension(60, 30);
	private static final Dimension listDimensions = new Dimension(200, 100);
	private static final JLabel lblTotalAmount = new JLabel("total amount");
	private static final JLabel lblName = new JLabel("Name");
	private static final JLabel lblDescription = new JLabel("Description");
	private static final JLabel lblTags = new JLabel("Tags");
	private static final JLabel lblIngredients = new JLabel("Ingredients");
	private static final JLabel lblIngredientName = new JLabel("Name");
	private static final JLabel lblAmount = new JLabel("Amount");

	private final JTextField txtName = new JTextField();
	private final JTextField txtDescription = new JTextField();
	private final JTextField txtTotalAmount = new JTextField();

	private final List<Tag> tags = new ArrayList<Tag>();
	private final DefaultListModel<Tag> lstTagModel = new DefaultListModel<Tag>();
	private final JList<Tag> lstTags = new JList<Tag>(this.lstTagModel);
	private final JTextField txtNewTag = new JTextField();
	private final JButton btnAddTag = new JButton("Add Tag");
	private final JButton btnDelTag = new JButton("Remove Tag");

	private final DefaultListModel<Ingredient> lstFormulaModel = new DefaultListModel<Ingredient>();
	private final JList<Ingredient> lstFormulas = new JList<Ingredient>(this.lstFormulaModel);
	private final JTextField txtNewIngredient = new JTextField();
	private final JTextField txtNewIngredientAmount = new JTextField();
	private final JButton btnAddIngredient = new JButton("Add ingedrient");
	private final JButton btnDelIngredient = new JButton("Del ingedrient");

	private final JButton btnSubmit = new JButton("Submit");

	public FormulaEditor() {
		super(new GridBagLayout());
		init(true);
		this.btnSubmit.addActionListener(new AddAction(this.txtName, this.txtDescription, this.txtTotalAmount, this.lstFormulaModel, this.tags));
	}

	public FormulaEditor(Formula formula) {
		super(new GridBagLayout());
		init(false);

		this.txtName.setText(formula.getName());
		this.txtDescription.setText(formula.getDescription());
		this.txtTotalAmount.setText(formula.getTotalAmount());

		try {
			// Add ingredients to the list
			List<Ingredient> ingredientList = new FormulaDAO().getIngredients(formula);
			for (Ingredient ingredient : ingredientList) {
				this.lstFormulaModel.addElement(ingredient);
			}

			// Add tags to the list
			Iterator<Tag> tagIterator = formula.getTags();
			while (tagIterator.hasNext()) {
				this.tags.add(tagIterator.next());
			}
			sortTags();
		}
		catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.btnSubmit.addActionListener(new SaveExistingAction(formula, this.txtName, this.txtDescription, this.txtTotalAmount, this.lstFormulaModel, this.tags));

	}

	private void init(boolean isNew) {
		int y = 0;

		// Formula requirements
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = y;
		this.add(lblName, constraints);

		constraints.gridx = 1;
		constraints.gridy = y;
		setDimensions(this.txtName, txtFieldDimensions);
		this.txtName.setEnabled(isNew);
		this.add(this.txtName, constraints);

		y++;
		constraints.gridx = 0;
		constraints.gridy = y;
		this.add(lblDescription, constraints);

		constraints.gridx = 1;
		constraints.gridy = y;
		setDimensions(this.txtDescription, txtFieldDimensions);
		this.add(this.txtDescription, constraints);

		y++;
		constraints.gridx = 0;
		constraints.gridy = y;
		setDimensions(lblTotalAmount, txtFieldDimensions);
		this.add(lblTotalAmount, constraints);

		constraints.gridx = 1;
		constraints.gridy = y;
		setDimensions(this.txtTotalAmount, txtFieldDimensions);
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
	private int addTagComponents(int y, GridBagConstraints constraints) {

		constraints.gridx = 0;
		constraints.gridy = y;
		constraints.gridwidth = 2;
		this.add(lblTags, constraints);
		constraints.gridwidth = 1;

		y++;
		constraints.gridx = 0;
		constraints.gridy = y;
		constraints.gridheight = 3;
		setDimensions(this.lstTags, listDimensions);
		this.add(this.lstTags, constraints);

		constraints.gridx = 1;
		constraints.gridy = y;
		constraints.gridheight = 1;
		setDimensions(this.txtNewTag, txtFieldDimensions);
		this.add(this.txtNewTag, constraints);

		y++;
		constraints.gridx = 1;
		constraints.gridy = y;
		this.btnAddTag.addActionListener(new AddTagAction(this.txtNewTag, this));
		this.add(this.btnAddTag, constraints);

		y++;
		constraints.gridx = 1;
		constraints.gridy = y;
		this.btnDelTag.addActionListener(new DelTagAction(this.lstTags, this));
		this.add(this.btnDelTag, constraints);
		return y;
	}

	/**
	 * This method adds the ingredient components to the GUI
	 *
	 * @param y the y value for the grid
	 * @param constraints the constraints to work with
	 * @return the y value of the last item
	 */
	private int addIngredientComponents(int y, GridBagConstraints constraints) {

		constraints.gridx = 0;
		constraints.gridy = y;
		constraints.gridwidth = 2;
		this.add(lblIngredients, constraints);
		constraints.gridwidth = 1;

		y++;
		constraints.gridx = 0;
		constraints.gridy = y;
		constraints.gridheight = 4;
		setDimensions(this.lstFormulas, listDimensions);
		this.add(this.lstFormulas, constraints);
		constraints.gridheight = 1;

		constraints.gridx = 1;
		constraints.gridy = y;
		setDimensions(lblIngredientName, txtFieldDimensions);
		this.add(lblIngredientName, constraints);

		constraints.gridx = 2;
		constraints.gridy = y;
		setDimensions(lblAmount, amountDimensions);
		this.add(lblAmount, constraints);

		y++;
		constraints.gridx = 1;
		constraints.gridy = y;
		setDimensions(this.txtNewIngredient, txtFieldDimensions);
		this.add(this.txtNewIngredient, constraints);

		constraints.gridx = 2;
		constraints.gridy = y;
		setDimensions(this.txtNewIngredientAmount, amountDimensions);
		this.add(this.txtNewIngredientAmount, constraints);

		y++;
		constraints.gridx = 1;
		constraints.gridy = y;
		this.btnAddIngredient.addActionListener(new AddIngredientAction(this.txtNewIngredient, this.txtNewIngredientAmount, this.lstFormulaModel));
		this.add(this.btnAddIngredient, constraints);

		y++;
		constraints.gridx = 1;
		constraints.gridy = y;
		this.btnDelIngredient.addActionListener(new DelIngredientAction(this.lstFormulas, this.lstFormulaModel));
		this.add(this.btnDelIngredient, constraints);
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
	private void setDimensions(JComponent comp, Dimension dim) {
		comp.setMinimumSize(dim);
		comp.setPreferredSize(dim);
		comp.setMaximumSize(dim);
	}

	/**
	 * This method adds a tag to the list of tags for this formula
	 *
	 * @param tag the tag to add
	 */
	public void addTag(Tag tag) {
		this.tags.add(tag);
		sortTags();
	}

	/**
	 * This method sorts the tags alphabetically in the list.
	 * should the list model and the backing list be out of sync, this method will synchronize them.
	 */
	public void sortTags() {
		this.tags.sort(new TagComparator());

		this.lstTagModel.clear();
		for (Tag tag : this.tags) {
			this.lstTagModel.addElement(tag);
		}
	}

	/**
	 * Remove a tag from the list.
	 * @param i the index of the tag to delete
	 */
	public void removeTag(int i) {
		this.tags.remove(i);
		this.lstTagModel.remove(i);
	}

}
