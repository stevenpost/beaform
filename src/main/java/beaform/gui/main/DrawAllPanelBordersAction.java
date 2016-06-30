package beaform.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An action that draws all borders on the MainPanel.
 *
 * @author Steven Post
 *
 */
public final class DrawAllPanelBordersAction implements ActionListener {

	private final MainPanel panel;

	public DrawAllPanelBordersAction(final MainPanel panel) {
		this.panel = panel;
	}

	@Override
	public void actionPerformed(final ActionEvent event) {
		this.panel.enableDebugBorders();
	}
}
