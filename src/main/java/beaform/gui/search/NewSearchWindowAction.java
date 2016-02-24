package beaform.gui.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.neo4j.graphdb.GraphDatabaseService;

public class NewSearchWindowAction implements ActionListener {

	private final JPanel panel;
	private final GraphDatabaseService graphDb;

	public NewSearchWindowAction(JPanel panel, GraphDatabaseService graphDb) {
		this.panel = panel;
		this.graphDb = graphDb;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.panel.add(new SearchGui(this.graphDb));
		this.panel.validate();
	}

}
