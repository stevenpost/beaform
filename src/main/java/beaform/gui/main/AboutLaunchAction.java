package beaform.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.gui.About;

/**
 * This action lets one launch the 'about' window.
 *
 * @author steven
 *
 */
final class AboutLaunchAction implements ActionListener {

	/**
	 * Invoked when the action is triggered.
	 *
	 * @param event the event object
	 */
	@Override
	public void actionPerformed(final ActionEvent event){
		javax.swing.SwingUtilities.invokeLater(new About());
	}
}