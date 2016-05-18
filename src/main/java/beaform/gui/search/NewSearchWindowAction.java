package beaform.gui.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.gui.MainGUI;

/**
 * This action opens up a new search.
 *
 * @author Steven Post
 *
 */
public class NewSearchWindowAction implements ActionListener {

	/**
	 * Invoked when the action occurs.
	 *
	 * @param event the event object
	 */
	@Override
	public void actionPerformed(final ActionEvent event) {
		MainGUI.getInstance().replaceWindow(new SearchGui());
	}

}
