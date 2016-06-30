package beaform.gui.search;

import java.util.ArrayList;
import java.util.List;

import beaform.entities.Formula;

/**
 * A task to to the actual rendering.
 *
 * @author Steven Post
 *
 */
final class AddFormTreeToGui implements Runnable {

	/** The result of the search */
	private final List<Formula> searchResult;

	/** The target panel */
	private final SearchGui pane;

	public AddFormTreeToGui(final List<Formula> searchResult, final SearchGui pane) {
		this.searchResult = new ArrayList<>(searchResult);
		this.pane = pane;
	}

	public AddFormTreeToGui(final Formula searchResult, final SearchGui pane) {
		this.searchResult = new ArrayList<>(1);
		this.searchResult.add(searchResult);
		this.pane = pane;
	}

	@Override
	public void run() {
		final FormulaTree formulaTree = new FormulaTree(this.searchResult);
		this.pane.setSearchResults(formulaTree);
	}
}
