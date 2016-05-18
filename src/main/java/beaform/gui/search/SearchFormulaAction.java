package beaform.gui.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Future;

import javax.swing.JPanel;
import javax.swing.JTextField;

import beaform.DbTaskHandler;
import beaform.SearchFormulaTask;
import beaform.entities.Formula;

/**
 * An action that kicks of a search
 *
 * @author Steven Post
 *
 */
public class SearchFormulaAction implements ActionListener {

	/** The text field with the name of a formula */
	private final JTextField txtName;

	/** The panel on which to render the result */
	private final JPanel pane;

	/**
	 * Constructor.
	 * @param name the text field with the name of the formula
	 * @param pane the panel on which to render the result
	 */
	public SearchFormulaAction(final JTextField name, final JPanel pane) {
		this.txtName = name;
		this.pane = pane;
	}

	/**
	 * Invoked when the action occurs.
	 *
	 * @param event the event object
	 */
	@Override
	public void actionPerformed(final ActionEvent event) {
		final Future<Formula> searchresult = DbTaskHandler.addTask(new SearchFormulaTask(this.txtName.getText()));
		DbTaskHandler.addTask(new RenderFormulaSearchResult(searchresult, this.pane));
	}

}
