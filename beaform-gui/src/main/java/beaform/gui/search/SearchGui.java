package beaform.gui.search;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.Future;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.entities.Formula;
import beaform.gui.InterchangableWindow;
import beaform.gui.InterchangableWindowDisplayer;
import beaform.search.SearchFormulaTask;
import beaform.search.SearchFormulasByTagTask;

/**
 * A search GUI.
 *
 * @author Steven Post
 *
 */
public final class SearchGui implements InterchangableWindow {

	private static final Logger LOG = LoggerFactory.getLogger(SearchGui.class);

	private final InterchangableWindowDisplayer icwd;
	private final JPanel panel = new JPanel(new BorderLayout());
	private final JTextField txtSearch = new JTextField();
	private final DefaultComboBoxModel<SearchType> comboBoxModel = new DefaultComboBoxModel<>(SearchType.values());

	/** The index of the formula tree on the target panel */
	private static final int FORMULA_TREE_LOC = 3;

	public SearchGui(InterchangableWindowDisplayer icwd) {
		this.icwd = icwd;
		final JPanel searchPanel =  createSearchPanel();
		this.panel.add(searchPanel, BorderLayout.PAGE_START);
		this.replace();
	}

	private JPanel createSearchPanel() {
		final Dimension textFieldSize = new Dimension(100, 30);
		final Dimension textFieldMaxSize = new Dimension(300, 30);

		final JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));

		final JTextField search = this.txtSearch;
		search.setMinimumSize(textFieldSize);
		search.setPreferredSize(textFieldSize);
		search.setMaximumSize(textFieldMaxSize);
		searchPanel.add(search);

		final JComboBox<SearchType> cmbType = new JComboBox<>(this.comboBoxModel);
		cmbType.setMinimumSize(textFieldSize);
		cmbType.setMaximumSize(textFieldMaxSize);
		cmbType.setPreferredSize(textFieldSize);
		cmbType.setEditable(false);
		cmbType.setSelectedIndex(0);
		searchPanel.add(cmbType);

		final JButton btnSearch = new JButton("Search");
		searchPanel.add(btnSearch);
		btnSearch.addActionListener(event -> search());

		return searchPanel;
	}

	public void search() {
		final SearchType searchType = (SearchType) this.comboBoxModel.getSelectedItem();

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
		final String searchText = this.txtSearch.getText();
		final SearchFormulasByTagTask task = new SearchFormulasByTagTask(searchText);
		final Future<List<Formula>> searchresult = SearchTaskHandler.addTask(task);
		SearchTaskHandler.addTask(new RenderFormulaSearchByTagResult(searchresult, this));
	}

	private void searchFormula() {
		final String searchText = this.txtSearch.getText();
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
		if (this.panel.getComponentCount() > FORMULA_TREE_LOC) {
			this.panel.remove(FORMULA_TREE_LOC);
		}
		formulaTree.addToPanel(this.panel, BorderLayout.CENTER);
		this.panel.revalidate();
	}

	@Override
	public final void replace() {
		this.icwd.replaceActiveWindow(this.panel);
	}

}
