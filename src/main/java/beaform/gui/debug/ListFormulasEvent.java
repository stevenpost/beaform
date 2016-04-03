package beaform.gui.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.GraphDbHandlerForJTA;
import beaform.debug.listFormulasTask;

public class ListFormulasEvent implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

		GraphDbHandlerForJTA.addTask(new listFormulasTask());
	}

}