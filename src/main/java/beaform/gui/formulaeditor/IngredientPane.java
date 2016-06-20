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
		final Dimension amountSize = new Dimension(60, 30);
		final Dimension listMinSize = new Dimension(200, 100);
		final Dimension nameSize = new Dimension(100, 30);

		final JLabel amountLabel = new JLabel("Amount");
		final JLabel ingredientsLabel = new JLabel("Ingredients", SwingConstants.CENTER);
		final JLabel nameLabel = new JLabel("Name");

		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 0.1;
		constraints.weighty = 0.1;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		int gridy = 0;

		constraints.gridx = 0;
		constraints.gridy = gridy;
		constraints.gridwidth = 3;
		Utilities.setBoldFont(ingredientsLabel);
		this.add(ingredientsLabel, constraints);
		constraints.gridwidth = 1;

		gridy++;
		constraints.gridx = 0;
		constraints.gridy = gridy;
		constraints.gridheight = 4;
		constraints.fill = GridBagConstraints.BOTH;
		this.lstFormulas.setMinimumSize(listMinSize);
		this.lstFormulas.setPreferredSize(listMinSize);
		this.add(this.lstFormulas, constraints);
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		constraints.gridx = 1;
		constraints.gridy = gridy;
		this.add(nameLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = gridy;
		this.add(amountLabel, constraints);

		gridy++;
		constraints.gridx = 1;
		constraints.gridy = gridy;
		this.txtName.setMinimumSize(nameSize);
		this.txtName.setPreferredSize(nameSize);
		this.txtName.setMaximumSize(nameSize);
		this.add(this.txtName, constraints);

		constraints.gridx = 2;
		constraints.gridy = gridy;
		this.txtAmount.setMinimumSize(amountSize);
		this.txtAmount.setPreferredSize(amountSize);
		this.txtAmount.setMaximumSize(amountSize);
		this.add(this.txtAmount, constraints);

		gridy++;
		constraints.gridx = 1;
		constraints.gridy = gridy;
		this.btnAddIngredient.addActionListener(event -> addNewIngredient());
		this.add(this.btnAddIngredient, constraints);

		gridy++;
		constraints.gridx = 1;
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
		final List<Ingredient> ingredients = new ArrayList<>();
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
