package beaform.gui.main;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * The help menu.
 *
 * @author Steven Post
 *
 */
class HelpMenu implements SubMenu {

	private final JMenu menu = new JMenu("Help");
	private final JMenuItem about = new JMenuItem("About...");

	public HelpMenu() {
		init();
	}

	private void init() {
		this.menu.add(this.about);
		this.about.addActionListener(new AboutLaunchAction());
	}

	@Override
	public void attachToMenuBar(final JMenuBar menuBar) {
		menuBar.add(this.menu);
	}

}
