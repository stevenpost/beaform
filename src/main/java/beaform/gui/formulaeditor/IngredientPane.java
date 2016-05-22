package beaform.gui.formulaeditor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.NoResultException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import beaform.entities.Formula;
import beaform.entities.FormulaDAO;
import beaform.entities.Ingredient;
import beaform.gui.Utilities;

/**
 * This class houses all the components for the Ingredients in the formula editor.
 *
 * @author Steven Post
 *
 */
public class IngredientPane extends JPanel {

	/**
	 * a serial
	 */
	private static final long serialVersionUID = 2342490028064073798L;

	/** A label for the list of ingredients */
	private static final JLabel LBL_INGREDIENTS = new JLabel("Ingredients");

	/** A label for the name of ingredients */
	private static final JLabel LBL_NAME = new JLabel("Name");

	/** A label for the amount in an ingredient */
	private static final JLabel LBL_AMOUNT = new JLabel("Amount");

	/** Dimensions for the amount field */
	private static final Dimension DIM_AMOUNT = new Dimension(60, 30);

	/** Dimensions for lists */
	private static final Dimension DIM_LISTS = new Dimension(200, 100);

	/** Dimensions for most text fields */
	private static final Dimension DIM_TEXTFIELDS = new Dimension(100, 30);

	/** A list model to get the list of formulas to the screen */
	private final DefaultListModel<Ingredient> ingredients = new DefaultListModel<Ingredient>();

	/** The graphical list ofr ingredients */
	private final JList<Ingredient> lstFormulas = new JList<Ingredient>(this.ingredients);

	/** A text field for the name of the ingredient */
	private final JTextField txtName = new JTextField();

	/** A text field to enter the amount of the ingredient that goes into the formula */
	private final JTextField txtAmount = new JTextField();

	/** A button to add an ingredient */
	private final JButton btnAddIngredient = new JButton("Add ingedrient");

	/** A button to remove an ingredient */
	private final JButton btnDelIngredient = new JButton("Del ingedrient");

	/**
	 * Constructor
	 */
	public IngredientPane() {
		super(new GridBagLayout());
		init();
	}

	private void init() {
		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 0.1;
		constraints.weighty = 0.1;

		int gridy = 0;

		constraints.gridx = 0;
		constraints.gridy = gridy;
		constraints.gridwidth = 3;
		Utilities.setBoldFont(LBL_INGREDIENTS);
		this.add(LBL_INGREDIENTS, constraints);
		constraints.gridwidth = 1;

		gridy++;
		constraints.gridx = 0;
		constraints.gridy = gridy;
		constraints.gridheight = 4;
		this.lstFormulas.setMinimumSize(DIM_LISTS);
		this.lstFormulas.setPreferredSize(DIM_LISTS);
		this.lstFormulas.setMaximumSize(DIM_LISTS);
		this.add(this.lstFormulas, constraints);
		constraints.gridheight = 1;

		constraints.gridx = 1;
		constraints.gridy = gridy;
		LBL_NAME.setMinimumSize(DIM_TEXTFIELDS);
		LBL_NAME.setPreferredSize(DIM_TEXTFIELDS);
		LBL_NAME.setMaximumSize(DIM_TEXTFIELDS);
		this.add(LBL_NAME, constraints);

		constraints.gridx = 2;
		constraints.gridy = gridy;
		LBL_AMOUNT.setMinimumSize(DIM_AMOUNT);
		LBL_AMOUNT.setPreferredSize(DIM_AMOUNT);
		LBL_AMOUNT.setMaximumSize(DIM_AMOUNT);
		this.add(LBL_AMOUNT, constraints);

		gridy++;
		constraints.gridx = 1;
		constraints.gridy = gridy;
		this.txtName.setMinimumSize(DIM_TEXTFIELDS);
		this.txtName.setPreferredSize(DIM_TEXTFIELDS);
		this.txtName.setMaximumSize(DIM_TEXTFIELDS);
		this.add(this.txtName, constraints);

		constraints.gridx = 2;
		constraints.gridy = gridy;
		this.txtAmount.setMinimumSize(DIM_AMOUNT);
		this.txtAmount.setPreferredSize(DIM_AMOUNT);
		this.txtAmount.setMaximumSize(DIM_AMOUNT);
		this.add(this.txtAmount, constraints);

		gridy++;
		constraints.gridx = 1;
		constraints.gridy = gridy;
		this.btnAddIngredient.addActionListener(new ActionListener() {

			/**
			 * Invoked when the button is pressed.
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				addNewIngredient();
			}
		});
		this.add(this.btnAddIngredient, constraints);

		gridy++;
		constraints.gridx = 1;
		constraints.gridy = gridy;
		this.btnDelIngredient.addActionListener(new ActionListener() {

			/**
			 * Invoked when the button is pressed.
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				removeSelectedIngredients();
			}
		});
		this.add(this.btnDelIngredient, constraints);
	}

	/**
	 * Add an ingredient to the list of ingredients.
	 *
	 * @param ingredient The ingredient to add
	 */
	public void addIngredient(final Ingredient ingredient) {
		this.ingredients.addElement(ingredient);
	}

	/**
	 * Add multiple ingredients to the list of ingredients.
	 *
	 * @param ingredients A list of ingredient to add
	 */
	public void addIngredients(final List<Ingredient> ingredients) {
		for (final Ingredient ingredient : ingredients) {
			this.ingredients.addElement(ingredient);
		}
	}

	/**
	 * Gets all the ingredients.
	 * @return all the ingredients
	 */
	public Iterator<Ingredient> getIngredients() {
		final List<Ingredient> ingredients = new ArrayList<Ingredient>();
		final int nrOfIngredients = this.ingredients.getSize();
		for (int i = 0; i < nrOfIngredients; i++) {
			ingredients.add(this.ingredients.getElementAt(i));
		}
		return ingredients.iterator();
	}

	/**
	 * Add a new ingredient to the list, based on the user input.
	 */
	public void addNewIngredient() {

		final String ingredient = this.txtName.getText();
		if ("".equals(ingredient)) {
			throw new UnsupportedOperationException("No formula name entered");
		}

		final String amount = this.txtAmount.getText();
		if ("".equals(amount)) {
			throw new UnsupportedOperationException("No amount entered");
		}

		final FormulaDAO formulaDAO = new FormulaDAO();

		try {
			final Formula form = formulaDAO.findFormulaByName(ingredient);
			this.ingredients.addElement(new Ingredient(form, amount));
		}
		catch (SystemException | NotSupportedException e1) {
			throw new IllegalStateException("Something went wrong when getting the existing formula", e1);
		}
		catch (NoResultException e) {
			throw new UnsupportedOperationException("The entered formula doesn't exist", e);
		}


		this.txtName.setText("");
		this.txtAmount.setText("");
	}

	/**
	 * Removes the selected ingredients from the view.
	 */
	public void removeSelectedIngredients() {
		while (!this.lstFormulas.isSelectionEmpty()) {
			final int selected = this.lstFormulas.getSelectedIndex();
			this.ingredients.remove(selected);
		}
	}

}
