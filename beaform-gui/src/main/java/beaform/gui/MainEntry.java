package beaform.gui;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.GraphDbHandler;
import beaform.gui.main.MainGUI;

/**
 * The main class to run this application.
 *
 * @author steven
 *
 */
public final class MainEntry {

	private static final Logger LOG = LoggerFactory.getLogger(MainEntry.class);

	private MainEntry() {
		// A utility class doesn't need a public constructor.
	}

	public static void main(final String[] args) {
		LOG.debug("Starting GUI...");

		try {
			javax.swing.SwingUtilities.invokeLater(new LookAndFeelInitiator());
			javax.swing.SwingUtilities.invokeAndWait(() -> MainGUI.getInstance());
		}
		catch (InvocationTargetException | InterruptedException e) {
			LOG.error(e.getMessage(), e);
		}

		LOG.debug("Initializing DB...");
		GraphDbHandler.initInstance("production_db");
		GraphDbHandler.getInstance();

		LOG.debug("Done");

	}

}
