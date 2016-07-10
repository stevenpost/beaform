package beaform.gui.main;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import beaform.debug.AsyncDebugUtils;

/**
 * The debug menu for the main menu.
 *
 * @author Steven Post
 *
 */
class DebugMenu extends JMenu {

	private static final long serialVersionUID = 7646144696014243808L;
	private final JMenuItem dbgAllFormulas = new JMenuItem("List all formulas");
	private final JMenuItem dbgFill = new JMenuItem("Fill DB");
	private final JMenuItem dbgClear = new JMenuItem("Clear DB");
	private final JMenuItem dbgBorders = new JMenuItem("Draw borders");
	private final MainPanel panel;

	public DebugMenu(final MainPanel panel) {
		super("Debug");
		this.panel = panel;
		init();
	}

	private void init() {
		this.add(this.dbgAllFormulas);
		this.dbgAllFormulas.addActionListener(event -> AsyncDebugUtils.listAllFormulas());

		this.add(this.dbgFill);
		this.dbgFill.addActionListener(event -> AsyncDebugUtils.fillDb());

		this.add(this.dbgClear);
		this.dbgClear.addActionListener(event -> AsyncDebugUtils.clearDb());

		this.add(this.dbgBorders);
		this.dbgBorders.addActionListener(event -> this.panel.enableDebugBorders());

	}
}
