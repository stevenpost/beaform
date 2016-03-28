package beaform.gui.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.GraphDbHandler;

public class ListBasesevent implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		GraphDbHandler.getInstance().listAllBases();
	}
}