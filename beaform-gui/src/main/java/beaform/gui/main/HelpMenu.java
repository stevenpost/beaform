package beaform.gui.main;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * The help menu.
 *
 * @author Steven Post
 *
 */
class HelpMenu extends JMenu {

	private static final long serialVersionUID = -3648158434819085351L;

	private final JMenuItem about = new JMenuItem("About...");

	public HelpMenu() {
		super("Help");
		init();
	}

	private void init() {
		this.add(this.about);
		this.about.addActionListener(new AboutLaunchAction());
	}

}
