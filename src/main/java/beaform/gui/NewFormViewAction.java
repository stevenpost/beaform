package beaform.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewFormViewAction implements ActionListener {

	private static Logger log = LoggerFactory.getLogger(NewFormViewAction.class);
	private final JPanel pane;

	public NewFormViewAction(JPanel pane) {
		this.pane = pane;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log.info("starting formTree");

		FormulaTree ft = new FormulaTree();
		this.pane.add(ft);
		this.pane.validate();
		log.info("end formTree");
	}



}
