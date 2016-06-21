package beaform.gui.search;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.entities.Formula;

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
	private final Future<Formula> searchresult;

	/** The target panel */
	private final SearchGui pane;

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

}
