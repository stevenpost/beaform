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
	/** A serial */
	private static final long serialVersionUID = -3648158434819085351L;

	/** The about item */
	private final JMenuItem about = new JMenuItem("About...");

	/**
	 * Creates a new instance.
	 */
	public HelpMenu() {
		super("Help");
		init();
	}

	private void init() {
		this.add(this.about);
		this.about.addActionListener(new AboutLaunchAction());
	}

}
