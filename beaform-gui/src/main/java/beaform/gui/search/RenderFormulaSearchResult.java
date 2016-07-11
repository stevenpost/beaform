package beaform.gui.search;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.entities.Formula;
import beaform.gui.InterchangableWindowDisplayer;

/**
 * This class is used to render the result of a formula search in the GUI.
 *
 * @author Steven Post
 *
 */
public final class RenderFormulaSearchResult implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(RenderFormulaSearchResult.class);

	private final InterchangableWindowDisplayer icwd;
	private final Future<Formula> searchresult;
	private final SearchGui targetPanel;

	public RenderFormulaSearchResult(final Future<Formula> searchresult,
	                                 final SearchGui pane,
	                                 final InterchangableWindowDisplayer icwd) {
		this.icwd = icwd;
		this.searchresult = searchresult;
		this.targetPanel = pane;
	}

	@Override
	public void run() {
		try {
			final Formula searchResult = this.searchresult.get();
			SwingUtilities.invokeLater(new AddFormTreeToGui(searchResult, this.targetPanel, this.icwd));
		}
		catch (InterruptedException | ExecutionException e1) {
			LOG.error("An error happened getting the result from the search.", e1);
			return;
		}
	}

}
