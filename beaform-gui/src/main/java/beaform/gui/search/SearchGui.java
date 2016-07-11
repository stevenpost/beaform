package beaform.gui.search;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.entities.Formula;
import beaform.gui.formulaeditor.FormulaEditor;
import beaform.gui.search.tree.FormulaTree;
import beaform.gui.subwindows.InterchangableWindow;
import beaform.search.SearchFormulaTask;
import beaform.search.SearchFormulasByTagTask;

/**
 * A search GUI.
 *
 * @author Steven Post
 *
 */
public final class SearchGui extends Observable implements InterchangableWindow, Observer {

	private static final Logger LOG = LoggerFactory.getLogger(SearchGui.class);

	private final SearchGuiUI searchguiUI;

	/** The index of the formula tree on the target panel */
	private static final int FORMULA_TREE_LOC = 3;

	public SearchGui() {
		this.searchguiUI = new SearchGuiUI(this);
	}

	public void search() {
		final SearchType searchType = this.searchguiUI.getSearchType();

		switch (searchType) {
			case FORMULA:
				searchFormula();
				break;
			case TAG:
				searchTag();
				break;
			default:
				if (LOG.isErrorEnabled()) {
					LOG.error(searchType + " is an unknown search");
				}
				break;
		}
	}

	private void searchTag() {
		final String searchText = this.searchguiUI.getSearchText();
		final SearchFormulasByTagTask task = new SearchFormulasByTagTask(searchText);
		final Future<List<Formula>> searchresult = SearchTaskHandler.addTask(task);
		SearchTaskHandler.addTask(new RenderFormulaSearchByTagResult(searchresult, this));
	}

	private void searchFormula() {
		final String searchText = this.searchguiUI.getSearchText();
		final SearchFormulaTask task = new SearchFormulaTask(searchText);
		final Future<Formula> searchresult = SearchTaskHandler.addTask(task);
		SearchTaskHandler.addTask(new RenderFormulaSearchResult(searchresult, this));
	}

	/**
	 * Set the search results in a view.
	 *
	 * @param formulaTree the search results
	 */
	public void setSearchResults(final FormulaTree formulaTree) {
		if (this.searchguiUI.getPanel().getComponentCount() > FORMULA_TREE_LOC) {
			this.searchguiUI.getPanel().remove(FORMULA_TREE_LOC);
		}
		formulaTree.addToPanel(this.searchguiUI.getPanel(), BorderLayout.CENTER);
		this.searchguiUI.getPanel().revalidate();
	}

	@Override
	public void replace() {
		this.notifyObservers(this.searchguiUI.getPanel());
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof FormulaTree && arg instanceof Formula) {
			Formula form = (Formula) arg;
			FormulaEditor editor = new FormulaEditor(form);
			this.notifyObservers(editor);
		}
	}

}
