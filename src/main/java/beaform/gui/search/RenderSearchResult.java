package beaform.gui.search;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.entities.Formula;
import beaform.gui.FormulaTree;

public final class RenderSearchResult implements Runnable { // NOPMD by steven on 5/16/16 4:31 PM

	/** A logger */
	private static final Logger LOG = LoggerFactory.getLogger(RenderSearchResult.class);
	private final transient Future<Formula> searchresult;
	private final transient JPanel pane;

	public RenderSearchResult(final Future<Formula> searchresult, final JPanel pane) {
		this.searchresult = searchresult;
		this.pane = pane;
	}

	@Override
	public void run() {
		Formula searchResult;
		try {
			searchResult = this.searchresult.get();
		}
		catch (InterruptedException | ExecutionException e1) {
			LOG.error("An error happened getting the result from the search.", e1);
			return;
		}
		SwingUtilities.invokeLater(new AddFormTreeToGui(searchResult, this.pane));
	}

	public static final class AddFormTreeToGui implements Runnable { // NOPMD by steven on 5/16/16 4:30 PM
		/** The index of the formula tree on the target panel */
		private static final int FORMULA_TREE_LOC = 3;

		private final Formula searchResult;
		private final JPanel pane;

		public AddFormTreeToGui(final Formula searchResult, final JPanel pane) {
			this.searchResult = searchResult;
			this.pane = pane;
		}

		@Override
		public void run() {
			final FormulaTree formulaTree = new FormulaTree(this.searchResult);
			if (this.pane.getComponentCount() > FORMULA_TREE_LOC) {
				this.pane.remove(FORMULA_TREE_LOC);
			}
			this.pane.add(formulaTree);
			this.pane.revalidate();
		}
	}
}