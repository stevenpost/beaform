package beaform.gui.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.GraphDbHandlerForJTA;
import beaform.debug.ClearDbTask;

public class ClearDbEvent implements ActionListener {

	@Override
	public void actionPerformed(final ActionEvent e) {
		GraphDbHandlerForJTA.addTask(new ClearDbTask());
	}
}
