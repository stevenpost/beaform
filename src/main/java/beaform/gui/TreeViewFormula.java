package beaform.gui;

import beaform.Ingredient;
import beaform.entities.Formula;

/**
 * This class represents a formula for use in a treeview
 *
 * @author steven
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
		this.formula = ingredient.getFormula();
		this.amount = ingredient.getAmount();
	}

	@Override
	public String toString() {
		return this.formula.getName();
	}

	public Formula getFormula() {
		return this.formula;
	}

	public String getAmount() {
		return this.amount;
	}

}
