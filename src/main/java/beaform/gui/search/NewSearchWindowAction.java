package beaform.gui.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.gui.MainGUI;

public class NewSearchWindowAction implements ActionListener {

	@Override
	public void actionPerformed(final ActionEvent e) {
		MainGUI.getInstance().replaceWindow(new SearchGui());
	}

}
