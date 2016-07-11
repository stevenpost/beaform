package beaform.gui.search.tree;

import java.util.List;
import java.util.Observable;

import javax.swing.JPanel;

import beaform.entities.Formula;

/**
 * This class implements a panel to show a tree view of formulas.
 *
 * @author Steven Post
 *
 */
public class FormulaTree extends Observable {

	private final FormulaTreeUI treeUi;

	public FormulaTree(final Formula formula) {
		this.treeUi = new FormulaTreeUI(this, formula);
	}

	public FormulaTree(final List<Formula> formulas) {
		this.treeUi = new FormulaTreeUI(this, formulas);
	}

	public void addToPanel(final JPanel panelToAttachTo, final Object constraints) {
		panelToAttachTo.add(this.treeUi.getPanel(), constraints);
	}

	public void editSelectedFormula() {
		final TreeViewFormula form = this.treeUi.getSelectedFormula();
		this.notifyObservers(form.getFormula());
	}

}
