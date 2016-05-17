package beaform.gui.formulaeditor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import beaform.Ingredient;

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
	private final transient DefaultListModel<Ingredient> ingredients = new DefaultListModel<Ingredient>();

	/**
	 * Constructor
	 */
	public IngredientPane() {
		super(new GridBagLayout());
		init();
	}

	private void init() {
		final GridBagConstraints constraints = new GridBagConstraints();

		int gridy = 0;
		final JList<Ingredient> lstFormulas = new JList<Ingredient>(this.ingredients);
		final JTextField txtName = new JTextField();
		final JTextField txtAmount = new JTextField();
		final JButton btnAddIngredient = new JButton("Add ingedrient");
		final JButton btnDelIngredient = new JButton("Del ingedrient");

		constraints.gridx = 0;
		constraints.gridy = gridy;
		constraints.gridwidth = 2;
		this.add(LBL_INGREDIENTS, constraints);
		constraints.gridwidth = 1;

		gridy++;
		constraints.gridx = 0;
		constraints.gridy = gridy;
		constraints.gridheight = 4;
		lstFormulas.setMinimumSize(DIM_LISTS);
		lstFormulas.setPreferredSize(DIM_LISTS);
		lstFormulas.setMaximumSize(DIM_LISTS);
		this.add(lstFormulas, constraints);
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
		txtName.setMinimumSize(DIM_TEXTFIELDS);
		txtName.setPreferredSize(DIM_TEXTFIELDS);
		txtName.setMaximumSize(DIM_TEXTFIELDS);
		this.add(txtName, constraints);

		constraints.gridx = 2;
		constraints.gridy = gridy;
		txtAmount.setMinimumSize(DIM_AMOUNT);
		txtAmount.setPreferredSize(DIM_AMOUNT);
		txtAmount.setMaximumSize(DIM_AMOUNT);
		this.add(txtAmount, constraints);

		gridy++;
		constraints.gridx = 1;
		constraints.gridy = gridy;
		btnAddIngredient.addActionListener(new AddIngredientAction(txtName, txtAmount, this.ingredients));
		this.add(btnAddIngredient, constraints);

		gridy++;
		constraints.gridx = 1;
		constraints.gridy = gridy;
		btnDelIngredient.addActionListener(new DelIngredientAction(lstFormulas, this.ingredients));
		this.add(btnDelIngredient, constraints);
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

}
