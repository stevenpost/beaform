package beaform.gui.search.tree;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class SelectedItemChangedAction implements TreeSelectionListener {

	private final FormulaTreeUI treeUI;

	public SelectedItemChangedAction(final FormulaTreeUI treeUI) {
		this.treeUI = treeUI;
	}

	/**
	 * Called when the selection in the tree view changes.
	 * It will display the details of the selected node.
	 */
	@Override
	public void valueChanged(final TreeSelectionEvent event) {
		final DefaultMutableTreeNode node = this.treeUI.getSelectedNode();

		if (node == null) {
			//Nothing is selected.
			return;
		}

		this.treeUI.fillDescription(node);
	}
}
