package beaform.gui.main;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import beaform.gui.MainPanel;
import beaform.gui.debug.ClearDbEvent;
import beaform.gui.debug.DrawAllPanelBordersAction;
import beaform.gui.debug.FillDbEvent;
import beaform.gui.debug.ListFormulasEvent;

/**
 * The debug menu for the main menu.
 *
 * @author Steven Post
 *
 */
class DebugMenu extends JMenu {

	/** A serial */
	private static final long serialVersionUID = 7646144696014243808L;

	/** List all the formulas */
	private final JMenuItem dbgAllFormulas = new JMenuItem("List all formulas");

	/** Fill the DB with dummy data */
	private final JMenuItem dbgFill = new JMenuItem("Fill DB");

	/** Clear the DB */
	private final JMenuItem dbgClear = new JMenuItem("Clear DB");

	/** Draw all JPanel borders */
	private final JMenuItem dbgBorders = new JMenuItem("Draw borders");

	/** The main panel */
	private final MainPanel panel;

	/**
	 * Creates a new instance.
	 * @param panel the main panel
	 */
	public DebugMenu(final MainPanel panel) {
		super("Debug");
		this.panel = panel;
		init();
	}

	private void init() {
		this.add(this.dbgAllFormulas);
		this.dbgAllFormulas.addActionListener(new ListFormulasEvent());

		this.add(this.dbgFill);
		this.dbgFill.addActionListener(new FillDbEvent());

		this.add(this.dbgClear);
		this.dbgClear.addActionListener(new ClearDbEvent());

		this.add(this.dbgBorders);
		this.dbgBorders.addActionListener(new DrawAllPanelBordersAction(this.panel));

	}
}