package beaform.gui.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.dao.DbTaskHandler;
import beaform.debug.FillDbTask;

/**
 * An event that fills the DB with test data.
 *
 * @author Steven Post
 *
 */
public class FillDbEvent implements ActionListener {

	/**
	 * Invoked when the action occurs.
	 *
	 * @param event the event object
	 */
	@Override
	public void actionPerformed(final ActionEvent event) {
		DbTaskHandler.addTask(new FillDbTask());
	}

}
