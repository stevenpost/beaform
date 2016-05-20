package beaform.gui.search;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.entities.Formula;
import beaform.gui.FormulaTree;

/**
 * This class is used to render the result of a formula search in the GUI.
 *
 * @author Steven Post
 *
 */
public final class RenderFormulaSearchResult implements Runnable {

	/** A logger */
	private static final Logger LOG = LoggerFactory.getLogger(RenderFormulaSearchResult.class);

	/** The task with the search result */
	private final transient Future<Formula> searchresult;

	/** The target panel */
	private final transient SearchGui pane;

	/**
	 * Constructor.
	 * @param searchresult The task for the search.
	 * @param pane The target panel.
	 */
	public RenderFormulaSearchResult(final Future<Formula> searchresult, final SearchGui pane) {
		this.searchresult = searchresult;
		this.pane = pane;
	}

	/**
	 * Invoked when the action occurs.
	 */
	@Override
	public void run() {
		try {
			final Formula searchResult = this.searchresult.get();
			SwingUtilities.invokeLater(new AddFormTreeToGui(searchResult, this.pane));
		}
		catch (InterruptedException | ExecutionException e1) {
			LOG.error("An error happened getting the result from the search.", e1);
			return;
		}
	}

	/**
	 * A task to do the actual rendering.
	 *
	 * @author Steven Post
	 *
	 */
	private static final class AddFormTreeToGui implements Runnable {

		/** The result of the search */
		private final Formula searchResult;

		/** The target panel */
		private final SearchGui pane;

		/**
		 * Constructor.
		 * @param searchResult the result of the search
		 * @param pane the target panel
		 */
		public AddFormTreeToGui(final Formula searchResult, final SearchGui pane) {
			this.searchResult = searchResult;
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
}