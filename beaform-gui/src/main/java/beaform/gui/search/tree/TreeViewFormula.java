package beaform.gui.search.tree;

import java.util.List;

import beaform.entities.Formula;
import beaform.entities.FormulaIngredient;
import beaform.entities.Ingredient;

/**
 * This class represents a formula for use in a tree view.
 *
 * @author Steven Post
 *
 */
public class TreeViewFormula {

	private final Formula formula;
	private final String amount;

	public TreeViewFormula(final Formula formula) {
		this.formula = formula;
		this.amount = "";
	}

	public TreeViewFormula(final Ingredient ingredient) {
		if (ingredient instanceof FormulaIngredient) {
			final FormulaIngredient formIngr = (FormulaIngredient) ingredient;
			this.formula = formIngr.getFormula();
		}
		else {
			throw new UnsupportedOperationException("Currently only ingredients based on formulas are supported");
		}
		this.amount = ingredient.getAmount();
	}

	public String getTotalAmount() {
		return this.formula.getTotalAmount();
	}

	@Override
	public String toString() {
		return this.formula.getName();
	}

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

	public List<String> getTagsAsStrings() {
		return this.formula.getTagsAsStrings();
	}

	public String getDescription() {
		return this.formula.getDescription();
	}

}
