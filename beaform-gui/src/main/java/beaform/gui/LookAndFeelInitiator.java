package beaform.gui;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LookAndFeelInitiator implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(LookAndFeelInitiator.class);

	@Override
	public void run() {
		//Set the look and feel.
		try{
			// Using OpenJDK, there is a bug that causes the
			// application to freeze when using a GTK look and feel.
			final boolean crossplatform = Boolean.getBoolean("beaform.useCrossPlatformLookAndFeel");
			if (crossplatform) {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
			else {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("Setting look and feel failed", e);
			}
		}

		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);
	}

}
