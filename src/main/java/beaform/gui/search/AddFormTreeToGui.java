package beaform.gui.search;

import java.util.ArrayList;
import java.util.List;

import beaform.entities.Formula;
import beaform.gui.FormulaTree;

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

	/**
	 * Constructor.
	 * @param searchResult the result of the search
	 * @param pane the target panel
	 */
	public AddFormTreeToGui(final List<Formula> searchResult, final SearchGui pane) {
		this.searchResult = new ArrayList<>(searchResult);
		this.pane = pane;
	}

	/**
	 * Constructor.
	 * @param searchResult the result of the search
	 * @param pane the target panel
	 */
	public AddFormTreeToGui(final Formula searchResult, final SearchGui pane) {
		this.searchResult = new ArrayList<>(1);
		this.searchResult.add(searchResult);
		this.pane = pane;
	}

	/**
	 * Invoked when the action occurs.
	 */
	@Override
	public void run() {
		final FormulaTree formulaTree = new FormulaTree(this.searchResult);
		this.pane.setSearchResults(formulaTree);
	}
}
