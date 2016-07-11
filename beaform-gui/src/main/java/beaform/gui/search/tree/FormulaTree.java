package beaform.gui.search.tree;

import java.util.List;

import javax.swing.JPanel;

import beaform.entities.Formula;
import beaform.gui.InterchangableWindowDisplayer;
import beaform.gui.formulaeditor.FormulaEditor;
import beaform.gui.search.TreeViewFormula;

/**
 * This class implements a panel to show a tree view of formulas.
 *
 * @author Steven Post
 *
 */
public class FormulaTree {

	private final InterchangableWindowDisplayer icwd;
	private final FormulaTreeUI treeUi;

	public FormulaTree(final InterchangableWindowDisplayer icwd, final Formula formula) {
		this.icwd = icwd;
		this.treeUi = new FormulaTreeUI(this, formula);
	}

	public FormulaTree(final InterchangableWindowDisplayer icwd, final List<Formula> formulas) {
		this.icwd = icwd;
		this.treeUi = new FormulaTreeUI(this, formulas);
	}

	public void addToPanel(final JPanel panelToAttachTo, final Object constraints) {
		panelToAttachTo.add(this.treeUi.getPanel(), constraints);
	}

	public void editSelectedFormula() {
		final TreeViewFormula form = this.treeUi.getSelectedFormula();
		launchFormulaEditor(form);
	}

	@SuppressWarnings("unused")
	private void launchFormulaEditor(final TreeViewFormula form) {
		new FormulaEditor(this.icwd, form.getFormula());
	}

}
