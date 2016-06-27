package beaform;

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

	/** a logger */
	private static final Logger LOG = LoggerFactory.getLogger(MainEntry.class);

	private MainEntry() {
		// A utility class doesn't need a public constructor.
	}

	/**
	 * The main method of the program.
	 *
	 * @param args
	 */
	public static void main(final String[] args) {
		LOG.debug("Starting GUI...");

		try {
			javax.swing.SwingUtilities.invokeAndWait(() -> MainGUI.createAndShowGUI());
		}
		catch (InvocationTargetException | InterruptedException e) {
			LOG.error(e.getMessage(), e);
		}

		LOG.debug("Initializing DB...");
		GraphDbHandler.initInstance("production");
		GraphDbHandler.getInstance();

		LOG.debug("Done");

	}

}
