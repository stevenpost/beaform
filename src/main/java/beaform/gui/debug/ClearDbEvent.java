package beaform.gui.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.dao.DbTaskHandler;
import beaform.debug.ClearDbTask;

/**
 * An event that clears the DB.
 *
 * @author Steven Post
 *
 */
public class ClearDbEvent implements ActionListener {

	@Override
	public void actionPerformed(final ActionEvent event) {
		DbTaskHandler.addTask(new ClearDbTask());
	}
}
