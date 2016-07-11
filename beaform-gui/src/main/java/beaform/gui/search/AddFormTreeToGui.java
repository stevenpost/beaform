package beaform.gui.search;

import java.util.ArrayList;
import java.util.List;

import beaform.entities.Formula;
import beaform.gui.search.tree.FormulaTree;

/**
 * A task to to the actual rendering.
 *
 * @author Steven Post
 *
 */
final class AddFormTreeToGui implements Runnable {

	private final List<Formula> searchResult;
	private final SearchGui targetPanel;

	public AddFormTreeToGui(final List<Formula> searchResult,
	                        final SearchGui pane) {

		this.searchResult = new ArrayList<>(searchResult);
		this.targetPanel = pane;
	}

	public AddFormTreeToGui(final Formula searchResult,
	                        final SearchGui pane) {
		this.searchResult = new ArrayList<>(1);
		this.searchResult.add(searchResult);
		this.targetPanel = pane;
	}

	@Override
	public void run() {
		final FormulaTree formulaTree = new FormulaTree(this.searchResult);
		formulaTree.addObserver(this.targetPanel);
		this.targetPanel.setSearchResults(formulaTree);
	}

}
