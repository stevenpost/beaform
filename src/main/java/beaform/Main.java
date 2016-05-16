package beaform;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.gui.MainGUI;

/**
 * The main class to run this application.
 *
 * @author steven
 *
 */
public class Main {

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		LOG.info("Starting GUI...");

		javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

			@Override
			public void run() {
				MainGUI.createAndShowGUI();
			}
		});

		LOG.info("Initializing DB...");
		GraphDbHandlerForJTA.getInstance();

		LOG.info("Done");

	}

}
