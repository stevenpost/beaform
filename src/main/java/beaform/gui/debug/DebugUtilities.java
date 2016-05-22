package beaform.gui.debug;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * A utility class to aid in debugging the layout.
 *
 * @author Steven Post
 *
 */
public final class DebugUtilities {

	/**
	 * Draw borders around the given {@link JPanel} and all descendants.
	 * @param panel the panel to draw borders on
	 */
	public static void drawBorders(final JPanel panel) {
		panel.setBorder(BorderFactory.createLineBorder(Color.RED));
		final Component[] components = panel.getComponents();
		for (final Component comp : components) {
			if (comp instanceof JPanel) {
				drawBorders((JPanel)comp);
			}
		}
		panel.revalidate();
	}

	private DebugUtilities() {
		// Utility classes don't needa public constructor.
	}
}
