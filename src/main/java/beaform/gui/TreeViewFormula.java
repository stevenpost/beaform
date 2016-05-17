package beaform.gui;

import java.util.List;

import beaform.Ingredient;
import beaform.entities.Formula;

/**
 * This class represents a formula for use in a tree view.
 *
 * @author Steven Post
 *
 */
public class TreeViewFormula {

	/** The formula this object represents */
	private final Formula formula;

	/** The amount of this formula in the total view of the tree */
	private final String amount;

	/**
	 * Constructor.
	 *
	 * @param formula The formula this object needs to represent
	 */
	public TreeViewFormula(final Formula formula) {
		this.formula = formula;
		this.amount = "";
	}

	/**
	 * Constructor.
	 *
	 * @param ingredient The ingredient this object needs to represent
	 */
	public TreeViewFormula(final Ingredient ingredient) {
		this.formula = ingredient.getFormula();
		this.amount = ingredient.getAmount();
	}

	/**
	 * A toString implementation
	 *
	 * @return name of the formula this object represents
	 */
	@Override
	public String toString() {
		return this.formula.getName();
	}

	/**
	 * Getter for the formula that this object is representing.
	 *
	 * @return The formula this object is representing
	 */
	public Formula getFormula() {
		return this.formula;
	}

	/**
	 * Getter for the amount of this formula in the whole.
	 * @return the amount
	 */
	public String getAmount() {
		return this.amount;
	}

	/**
	 * Get a list of tags as strings
	 * @return a list of strings
	 */
	public List<String> getTagsAsStrings() {
		return this.formula.getTagsAsStrings();
	}

	/**
	 * Getter for the formula description
	 * @return the description of the formula
	 */
	public String getDescription() {
		return this.formula.getDescription();
	}

}
