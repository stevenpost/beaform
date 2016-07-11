package beaform.gui.search;

import java.util.ArrayList;
import java.util.List;

import beaform.entities.Formula;
import beaform.gui.InterchangableWindowDisplayer;
import beaform.gui.search.tree.FormulaTree;

/**
 * A task to to the actual rendering.
 *
 * @author Steven Post
 *
 */
final class AddFormTreeToGui implements Runnable {

	private final InterchangableWindowDisplayer icwd;
	private final List<Formula> searchResult;
	private final SearchGui targetPanel;

	public AddFormTreeToGui(final List<Formula> searchResult,
	                        final SearchGui pane,
	                        final InterchangableWindowDisplayer icwd) {

		this.searchResult = new ArrayList<>(searchResult);
		this.targetPanel = pane;
		this.icwd = icwd;
	}

	public AddFormTreeToGui(final Formula searchResult,
	                        final SearchGui pane,
	                        final InterchangableWindowDisplayer icwd) {
		this.searchResult = new ArrayList<>(1);
		this.searchResult.add(searchResult);
		this.targetPanel = pane;
		this.icwd = icwd;
	}

	@Override
	public void run() {
		final FormulaTree formulaTree = new FormulaTree(this.icwd, this.searchResult);
		this.targetPanel.setSearchResults(formulaTree);
	}

}
