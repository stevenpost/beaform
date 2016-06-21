package beaform.gui.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.gui.main.MainPanel;

/**
 * An action that draws all borders on the MainPanel.
 *
 * @author Steven Post
 *
 */
public final class DrawAllPanelBordersAction implements ActionListener {

	/** The panel */
	private final MainPanel panel;

	/**
	 * Constructor
	 * @param panel the panel on which to draw the borders
	 */
	public DrawAllPanelBordersAction(final MainPanel panel) {
		this.panel = panel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void actionPerformed(final ActionEvent event) {
		this.panel.enableDebugBorders();
	}
}
