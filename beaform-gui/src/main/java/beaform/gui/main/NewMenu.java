package beaform.gui.main;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import beaform.gui.formulaeditor.FormulaEditor;
import beaform.gui.search.SearchGui;

/**
 * The new menu for the application.
 *
 * @author Steven Post
 *
 */
class NewMenu implements SubMenu {

	private final JMenu menu = new JMenu("New...");
	private final JMenuItem search = new JMenuItem("Search");

	/** The add item, which opens up the formula editor */
	private final JMenuItem add = new JMenuItem("Add");

	public NewMenu() {
		init();
	}

	private void init() {
		this.menu.add(this.search);
		this.search.addActionListener(event -> new SearchGui(MainGUI.getInstance()));

		this.menu.add(this.add);
		this.add.addActionListener(event -> new FormulaEditor(MainGUI.getInstance()));
	}

	@Override
	public void attachToMenuBar(final JMenuBar menubar) {
		menubar.add(this.menu);
	}

}
