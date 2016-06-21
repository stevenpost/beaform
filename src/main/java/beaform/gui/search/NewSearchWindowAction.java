package beaform.gui.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.gui.main.MainPanel;

/**
 * This action opens up a new search.
 *
 * @author Steven Post
 *
 */
public class NewSearchWindowAction implements ActionListener {

	/** Panel for the new content */
	private final MainPanel panel;

	/**
	 * Constructor
	 *
	 * @param panel the target panel
	 */
	public NewSearchWindowAction(final MainPanel panel) {
		this.panel = panel;
	}

	/**
	 * Invoked when the action occurs.
	 *
	 * @param event the event object
	 */
	@Override
	public void actionPerformed(final ActionEvent event) {
		this.panel.replaceWindow(new SearchGui());
	}

}
