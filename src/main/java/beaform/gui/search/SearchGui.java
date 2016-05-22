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

	/** The dimensions for most text fields */
	private static final Dimension DIM_TXTFIELDS = new Dimension(100, 30);

	/** The dimensions for most text fields */
	private static final Dimension DIM_MAX_TXTFIELDS = new Dimension(300, 30);

	/** The field to type in the search */
	private final JTextField txtSearchTag = new JTextField();

	/** A button to kickoff the search */
	private final JButton btnSearch = new JButton("Search");

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
		final JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));

		this.txtSearchTag.setMinimumSize(DIM_TXTFIELDS);
		this.txtSearchTag.setPreferredSize(DIM_TXTFIELDS);
		this.txtSearchTag.setMaximumSize(DIM_MAX_TXTFIELDS);
		searchPanel.add(this.txtSearchTag);

		final JComboBox<SearchType> cmbType = new JComboBox<SearchType>(this.comboBoxModel);
		cmbType.setMinimumSize(DIM_TXTFIELDS);
		cmbType.setMaximumSize(DIM_MAX_TXTFIELDS);
		cmbType.setPreferredSize(DIM_TXTFIELDS);
		cmbType.setEditable(false);
		cmbType.setSelectedIndex(0);
		searchPanel.add(cmbType);

		searchPanel.add(this.btnSearch);
		this.btnSearch.addActionListener(new ActionListener() {

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
				final Future<Formula> searchresult = DbTaskHandler.addTask(new SearchFormulaTask(this.txtSearchTag.getText()));
				VariousTaskHandler.addTask(new RenderFormulaSearchResult(searchresult, this));
				break;
			}
			case TAG:
			{
				final Future<List<Formula>> searchresult = DbTaskHandler.addTask(new SearchFormulasByTagTask(this.txtSearchTag.getText()));
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
