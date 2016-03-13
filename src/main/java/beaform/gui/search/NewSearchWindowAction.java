package beaform.gui.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class NewSearchWindowAction implements ActionListener {

	private final JPanel panel;

	public NewSearchWindowAction(JPanel panel) {
		this.panel = panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.panel.add(new SearchGui());
		this.panel.validate();
	}

}
