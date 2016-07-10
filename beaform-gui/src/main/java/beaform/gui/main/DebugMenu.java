package beaform.gui.main;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import beaform.debug.AsyncDebugUtils;

/**
 * The debug menu for the main menu.
 *
 * @author Steven Post
 *
 */
class DebugMenu implements SubMenu {

	private final JMenu menu = new JMenu("Debug");
	private final JMenuItem dbgAllFormulas = new JMenuItem("List all formulas");
	private final JMenuItem dbgFill = new JMenuItem("Fill DB");
	private final JMenuItem dbgClear = new JMenuItem("Clear DB");
	private final JMenuItem dbgBorders = new JMenuItem("Draw borders");
	private final MainPanel panel;

	public DebugMenu(final MainPanel panel) {
		this.panel = panel;
		init();
	}

	private void init() {
		this.menu.add(this.dbgAllFormulas);
		this.dbgAllFormulas.addActionListener(event -> AsyncDebugUtils.listAllFormulas());

		this.menu.add(this.dbgFill);
		this.dbgFill.addActionListener(event -> AsyncDebugUtils.fillDb());

		this.menu.add(this.dbgClear);
		this.dbgClear.addActionListener(event -> AsyncDebugUtils.clearDb());

		this.menu.add(this.dbgBorders);
		this.dbgBorders.addActionListener(event -> this.panel.enableDebugBorders());
	}

	@Override
	public void attachToMenuBar(JMenuBar menuBar) {
		menuBar.add(this.menu);
	}
}
