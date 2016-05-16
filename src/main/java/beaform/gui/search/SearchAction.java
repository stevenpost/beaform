package beaform.gui.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Future;

import javax.swing.JPanel;
import javax.swing.JTextField;

import beaform.GraphDbHandlerForJTA;
import beaform.SearchFormulaTask;
import beaform.entities.Formula;

public class SearchAction implements ActionListener {

	private final JTextField txtName;
	private final JPanel pane;

	public SearchAction(final JTextField name, final JPanel pane) {
		this.txtName = name;
		this.pane = pane;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		final Future<Formula> searchresult = GraphDbHandlerForJTA.addTask(new SearchFormulaTask(this.txtName.getText()));
		GraphDbHandlerForJTA.addTask(new RenderSearchResult(searchresult, this.pane));

	}

}
