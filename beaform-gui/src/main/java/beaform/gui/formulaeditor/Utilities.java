package beaform.gui.formulaeditor;

import java.awt.Component;
import java.awt.Font;

/**
 * A utility class that helps in modifying some GUI components.
 *
 * @author Steven Post
 *
 */
public final class Utilities {

	private Utilities() {
		// Utility classes don't need public constructors.
	}

	public static void setBoldFont(final Component comp) {
		final Font font = comp.getFont();
		final Font boldFont = createBoldFont(font);
		comp.setFont(boldFont);
	}

	public static Font createBoldFont(final Font font) {
		return new Font(font.getName(), Font.BOLD, font.getSize());
	}

}
