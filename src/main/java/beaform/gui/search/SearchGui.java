package beaform.gui.search;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import beaform.DbTaskHandler;
import beaform.SearchFormulaTask;
import beaform.SearchFormulasByTagTask;
import beaform.VariousTaskHandler;
import beaform.entities.Formula;
import beaform.gui.FormulaTree;

/**
 * A search GUI.
 *
 * @author Steven Post
 *
 */
public class SearchGui extends JPanel {

	/** A serial. */
	private static final long serialVersionUID = 2557014310487638917L;

	/** A logger */
	private static final Logger LOG = LoggerFactory.getLogger(SearchGui.class);

	/** The field to type in the search */
	private final JTextField txtSearch = new JTextField();

	/** The model for the combo box. */
	private final DefaultComboBoxModel<SearchType> comboBoxModel = new DefaultComboBoxModel<SearchType>(SearchType.values());

	/** The index of the formula tree on the target panel */
	private static final int FORMULA_TREE_LOC = 3;

	/**
	 * Constructor.
	 */
	public SearchGui() {
		super(new BorderLayout());

		final JPanel searchPanel =  createSearchPanel();
		this.add(searchPanel, BorderLayout.PAGE_START);

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

		final JComboBox<SearchType> cmbType = new JComboBox<SearchType>(this.comboBoxModel);
		cmbType.setMinimumSize(textFieldSize);
		cmbType.setMaximumSize(textFieldMaxSize);
		cmbType.setPreferredSize(textFieldSize);
		cmbType.setEditable(false);
		cmbType.setSelectedIndex(0);
		searchPanel.add(cmbType);

		final JButton btnSearch = new JButton("Search");
		searchPanel.add(btnSearch);
		btnSearch.addActionListener(new ActionListener() {

			/**
			 * Invoked when the action occurs.
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				search();
			}
		});

		return searchPanel;
	}

	/**
	 * Kickoff the search.
	 */
	public void search() {
		final SearchType searchType = (SearchType) this.comboBoxModel.getSelectedItem();

		switch (searchType) {
			case FORMULA:
			{
				final String searchText = this.txtSearch.getText();
				final SearchFormulaTask task = new SearchFormulaTask(searchText);
				final Future<Formula> searchresult = DbTaskHandler.addTask(task);
				VariousTaskHandler.addTask(new RenderFormulaSearchResult(searchresult, this));
				break;
			}
			case TAG:
			{
				final String searchText = this.txtSearch.getText();
				final SearchFormulasByTagTask task = new SearchFormulasByTagTask(searchText);
				final Future<List<Formula>> searchresult = DbTaskHandler.addTask(task);
				VariousTaskHandler.addTask(new RenderFormulaSearchByTagResult(searchresult, this));
				break;
			}
			default:
				if (LOG.isErrorEnabled()) {
					LOG.error(searchType + " is an unknown search");
				}
				break;
		}
	}

	/**
	 * Set the search results in a view.
	 *
	 * @param formulaTree the search results
	 */
	public void setSearchResults(final FormulaTree formulaTree) {
		if (this.getComponentCount() > FORMULA_TREE_LOC) {
			this.remove(FORMULA_TREE_LOC);
		}
		this.add(formulaTree, BorderLayout.CENTER);
		this.revalidate();
	}

}
