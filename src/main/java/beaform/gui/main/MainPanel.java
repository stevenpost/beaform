package beaform.gui.main;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import beaform.gui.debug.DebugUtilities;

/**
 * This class represents the main panel where all views will work in.
 *
 * @author Steven Post
 *
 */
public final class MainPanel extends JPanel {

	private static final long serialVersionUID = 1207348877338520359L;

	/** An inner panel for the actual content */
	private final JPanel panel = new JPanel(new BorderLayout());
	private final JScrollPane scrollPane = new JScrollPane(this.panel);

	public MainPanel() {
		super(new BorderLayout());
		this.add(this.scrollPane);
	}

	/**
	 * This method will replace the current active window.
	 *
	 * @param comp The new window to display
	 */
	public void replaceWindow(final Component comp) {
		if (this.panel.getComponentCount() > 0) {
			this.panel.remove(0);
		}
		this.panel.add(comp);
		this.panel.revalidate();
	}

	public void enableDebugBorders() {
		DebugUtilities.drawBorders(this.panel);
	}

}
