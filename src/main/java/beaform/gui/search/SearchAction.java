package beaform.gui.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.Future;

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

/**
 * An action that kicks of a search
 *
 * @author Steven Post
 *
 */
public class SearchAction implements ActionListener {

	/** A logger */
	private static final Logger LOG = LoggerFactory.getLogger(SearchAction.class);

	/** The text field with the name of a formula */
	private final JTextField txtName;

	/** The panel on which to render the result */
	private final JPanel pane;

	/** The combo box to select a type of search */
	private final JComboBox<String> searchType;

	/**
	 * Constructor.
	 * @param name the text field with the name of the formula
	 * @param pane the panel on which to render the result
	 */
	public SearchAction(final JTextField name, final JPanel pane, final JComboBox<String> searchType) {
		this.txtName = name;
		this.pane = pane;
		this.searchType = searchType;
	}

	/**
	 * Invoked when the action occurs.
	 *
	 * @param event the event object
	 */
	@Override
	public void actionPerformed(final ActionEvent event) {
		if ("Formula".equals(this.searchType.getSelectedItem())) {
			final Future<Formula> searchresult = DbTaskHandler.addTask(new SearchFormulaTask(this.txtName.getText()));
			VariousTaskHandler.addTask(new RenderFormulaSearchResult(searchresult, this.pane));
		}
		else if ("Tag".equals(this.searchType.getSelectedItem())) {
			final Future<List<Formula>> searchresult = DbTaskHandler.addTask(new SearchFormulasByTagTask(this.txtName.getText()));
			VariousTaskHandler.addTask(new RenderFormulaSearchByTagResult(searchresult, this.pane));
		}
		else {
			if (LOG.isErrorEnabled()) {
				LOG.error(this.searchType.getSelectedItem() + " is an unknown search");
			}
		}
	}

}
