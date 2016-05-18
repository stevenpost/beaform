package beaform.gui.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.GraphDbHandlerForJTA;
import beaform.debug.ClearDbTask;

/**
 * An event that clears the DB.
 *
 * @author Steven Post
 *
 */
public class ClearDbEvent implements ActionListener {

	/**
	 * Invoked when the action occurs.
	 *
	 * @param event the event object
	 */
	@Override
	public void actionPerformed(final ActionEvent event) {
		GraphDbHandlerForJTA.addTask(new ClearDbTask());
	}
}
