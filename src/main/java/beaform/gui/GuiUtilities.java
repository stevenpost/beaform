package beaform.gui;

import java.awt.Component;

import beaform.gui.main.MainGUI;

/**
 * This class contains some utilities to aid in working with the GUI.
 *
 * @author Steven Post
 *
 */
public final class GuiUtilities {

	private GuiUtilities() {
		// A utility class doens't have public constructors.
	}

	/**
	 * Replace the current window in the main GUI.
	 * @param comp the new component
	 */
	public static void replaceWindow(final Component comp) {
		MainGUI.getInstance().replaceWindow(comp);
	}

}
