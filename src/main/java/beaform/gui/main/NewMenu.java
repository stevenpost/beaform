package beaform.gui.main;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import beaform.gui.formulaeditor.FormulaEditor;
import beaform.gui.search.SearchGui;

/**
 * The new menu for the application.
 *
 * @author Steven Post
 *
 */
class NewMenu extends JMenu {

	private static final long serialVersionUID = -6144314332877169796L;

	private final JMenuItem search = new JMenuItem("Search");

	/** The add item, which opens up the formula editor */
	private final JMenuItem add = new JMenuItem("Add");

	public NewMenu() {
		super("New...");
		init();
	}

	private void init() {
		this.add(this.search);
		this.search.addActionListener(event -> MainGUI.getInstance().replaceActiveWindow(new SearchGui()));

		this.add(this.add);
		this.add.addActionListener(event -> MainGUI.getInstance().replaceActiveWindow(new FormulaEditor()));
	}

}
