package beaform.gui.main;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * The new menu for the application.
 *
 * @author Steven Post
 *
 */
class NewMenu extends JMenu {

	/** A serial */
	private static final long serialVersionUID = -6144314332877169796L;

	/** The main panel */
	private final MainPanel panel;

	/** The search item */
	private final JMenuItem search = new JMenuItem("Search");

	/** The add item, which opens up the formula editor */
	private final JMenuItem add = new JMenuItem("Add");

	/**
	 * Creates a new instance.
	 *
	 * @param panel the main panel
	 */
	public NewMenu(final MainPanel panel) {
		super("New...");
		this.panel = panel;
		init();
	}

	private void init() {
		this.add(this.search);
		this.search.addActionListener(new NewSearchWindowAction(this.panel));

		this.add(this.add);
		this.add.addActionListener(new NewAddWindowAction(this.panel));
	}

}
