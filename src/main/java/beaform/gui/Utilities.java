package beaform.gui;

import java.awt.Component;
import java.awt.Font;

/**
 * A utility class that helps in modifying some GUI components.
 *
 * @author Steven Post
 *
 */
public final class Utilities {

	/**
	 * Set a bold font on the component.
	 */
	public static void setBoldFont(final Component comp) {
		final Font font = comp.getFont();
		final Font boldFont = createBoldFont(font);
		comp.setFont(boldFont);
	}

	/**
	 * Create a bold variant on a font.
	 * @param font the original font
	 * @return the bold variant
	 */
	public static Font createBoldFont(final Font font) {
		return new Font(font.getName(), Font.BOLD, font.getSize());
	}

	private Utilities() {
		// Utility classes don't need public constructors.
	}

}
