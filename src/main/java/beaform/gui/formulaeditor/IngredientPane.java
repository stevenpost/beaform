package beaform.gui.formulaeditor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.SwingConstants;

import beaform.entities.Formula;
import beaform.entities.FormulaDAO;
import beaform.entities.Ingredient;
import beaform.entities.TransactionSetupException;
import beaform.gui.Utilities;

/**
 * This class houses all the components for the Ingredients in the formula editor.
 *
 * @author Steven Post
 *
 */
public final class IngredientPane extends JPanel {

	/**
	 * a serial
	 */
	private static final long serialVersionUID = 2342490028064073798L;

	/** width for the amount field */
	private static final int AMOUNT_WIDTH = 60;

	/** height for the amount field */
	private static final int AMOUNT_HEIGHT = 30;

	/** minimum width for the list */
	private static final int MIN_LIST_WIDTH = 200;

	/** minimum height for the list */
	private static final int MIN_LIST_HEIGHT = 100;

	/** width for the name field */
	private static final int NAME_WIDTH = 100;

	/** height for the name field */
	private static final int NAME_HEIGHT = 30;

	/** default weigthx for the constraints */
	private static final double DEFAULT_X_WEIGTH = 0.1;

	/** default weigthy for the constraints */
	private static final double DEFAULT_Y_WEIGTH = 0.1;

	/** number of columns for the ingredient label */
	private static final int FORMULALBL_COLS = 3;

	/** number of rows for the list of ingredients */
	private static final int FORMULALIST_ROWS = 4;

	/** A list model to get the list of formulas to the screen */
	private final DefaultListModel<Ingredient> ingredients = new DefaultListModel<>();

	/** The graphical list ofr ingredients */
	private final JList<Ingredient> lstFormulas = new JList<>(this.ingredients);

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
		final Dimension amountSize = new Dimension(AMOUNT_WIDTH, AMOUNT_HEIGHT);
		final Dimension listMinSize = new Dimension(MIN_LIST_WIDTH, MIN_LIST_HEIGHT);
		final Dimension nameSize = new Dimension(NAME_WIDTH, NAME_HEIGHT);

		final JLabel amountLabel = new JLabel("Amount");
		final JLabel ingredientsLabel = new JLabel("Ingredients", SwingConstants.CENTER);
		final JLabel nameLabel = new JLabel("Name");

		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = DEFAULT_X_WEIGTH;
		constraints.weighty = DEFAULT_Y_WEIGTH;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		int gridx = 0;
		int gridy = 0;

		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridwidth = FORMULALBL_COLS;
		Utilities.setBoldFont(ingredientsLabel);
		this.add(ingredientsLabel, constraints);
		constraints.gridwidth = 1;

		gridy++;
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridheight = FORMULALIST_ROWS;
		constraints.fill = GridBagConstraints.BOTH;
		this.lstFormulas.setMinimumSize(listMinSize);
		this.lstFormulas.setPreferredSize(listMinSize);
		this.add(this.lstFormulas, constraints);
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		gridx++;
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		this.add(nameLabel, constraints);

		gridx++;
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		this.add(amountLabel, constraints);

		gridx = 1;
		gridy++;
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		this.txtName.setMinimumSize(nameSize);
		this.txtName.setPreferredSize(nameSize);
		this.txtName.setMaximumSize(nameSize);
		this.add(this.txtName, constraints);

		gridx++;
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		this.txtAmount.setMinimumSize(amountSize);
		this.txtAmount.setPreferredSize(amountSize);
		this.txtAmount.setMaximumSize(amountSize);
		this.add(this.txtAmount, constraints);

		gridx = 1;
		gridy++;
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		this.btnAddIngredient.addActionListener(event -> addNewIngredient());
		this.add(this.btnAddIngredient, constraints);

		gridy++;
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		this.btnDelIngredient.addActionListener(event -> removeSelectedIngredients());
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
	 * @param ingredientList A list of ingredient to add
	 */
	public void addIngredients(final List<Ingredient> ingredientList) {
		for (final Ingredient ingredient : ingredientList) {
			this.ingredients.addElement(ingredient);
		}
	}

	/**
	 * Gets all the ingredients.
	 * @return all the ingredients
	 */
	public Iterator<Ingredient> getIngredients() {
		final List<Ingredient> returnIngredients = new ArrayList<>();
		final int nrOfIngredients = this.ingredients.getSize();
		for (int i = 0; i < nrOfIngredients; i++) {
			returnIngredients.add(this.ingredients.getElementAt(i));
		}
		return returnIngredients.iterator();
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

		try {
			final Formula form = FormulaDAO.findFormulaByName(ingredient);
			this.ingredients.addElement(new Ingredient(form, amount));
		}
		catch (NoResultException e) {
			throw new UnsupportedOperationException("The entered formula doesn't exist", e);
		}
		catch (TransactionSetupException e) {
			throw new IllegalStateException("Something went wrong when getting the existing formula", e);
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
