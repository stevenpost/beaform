package beaform.gui.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.dao.DbTaskHandler;
import beaform.debug.ListFormulasTask;

/**
 * An event that lists all the formulas.
 *
 * @author Steven Post
 *
 */
public class ListFormulasEvent implements ActionListener {

	@Override
	public void actionPerformed(final ActionEvent event) {

		DbTaskHandler.addTask(new ListFormulasTask());
	}

}
