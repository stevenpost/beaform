package beaform;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	public static void main(final String[] args) throws InvocationTargetException, InterruptedException { // NOPMD by steven on 5/22/16 2:17 PM
		LOG.debug("Starting GUI...");

		javax.swing.SwingUtilities.invokeAndWait(() -> MainGUI.createAndShowGUI());

		LOG.debug("Initializing DB...");
		GraphDbHandlerForJTA.getInstance();

		LOG.debug("Done");

	}

}
