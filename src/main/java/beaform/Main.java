package beaform;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.gui.MainGUI;

public class Main {

	private static Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		log.info("start");

		GraphDbHandler.getInstance().setClearDbOnExit(true);

		javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

			@Override
			public void run() {
				MainGUI.createAndShowGUI();
			}
		});
		log.info("Done");
	}

}
