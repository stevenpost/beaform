package beaform.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.gui.debug.ClearDbEvent;
import beaform.gui.debug.FillDbEvent;
import beaform.gui.debug.ListFormulasEvent;
import beaform.gui.search.NewSearchWindowAction;


/**
 * This class represents the main user interface.
 * @author Steven
 *
 */
public class MainGUI {

	/** A logger */
	private static final Logger LOG = LoggerFactory.getLogger(MainGUI.class);

	/** The main instance of the GUI */
	private static MainGUI instance;

	/** The main frame (or window) */
	private static JFrame frm = new JFrame("BeaForm");

	/** The main panel */
	private final MainPanel panel = new MainPanel();

	private void init(){
		final JMenuBar menu = createMenu();
		frm.setJMenuBar(menu);

		LOG.debug("end init");
	}

	/**
	 * This method will replace the current active window.
	 *
	 * @param comp The new window to display
	 */
	public void replaceWindow(final Component comp) {
		this.panel.replaceWindow(comp);
	}

	private JPanel getPanel() {
		return this.panel;
	}

	private JMenuBar createMenu() {
		final JMenuBar menu = new JMenuBar();
		menu.add(createNewMenu());
		menu.add(createHelpMenu());
		menu.add(createDebugMenu());

		return menu;
	}

	private JMenu createNewMenu() {
		final JMenu newMenuItem = new JMenu("New...");

		final JMenuItem search = new JMenuItem("Search");
		newMenuItem.add(search);
		search.addActionListener(new NewSearchWindowAction(this.panel));

		final JMenuItem add = new JMenuItem("Add");
		newMenuItem.add(add);
		add.addActionListener(new NewAddWindowAction(this.panel));

		return newMenuItem;
	}

	private JMenu createHelpMenu() {
		final JMenu helpmenu = new JMenu("Help");

		final JMenuItem about = new JMenuItem("About...");
		helpmenu.add(about);
		about.addActionListener(new AboutLaunchAction());

		return helpmenu;
	}

	private JMenu createDebugMenu() {
		final JMenu debugMenuItem = new JMenu("Debug");

		final JMenuItem dbgAllFormulas = new JMenuItem("List all formulas");
		debugMenuItem.add(dbgAllFormulas);
		dbgAllFormulas.addActionListener(new ListFormulasEvent());

		final JMenuItem dbgFill = new JMenuItem("Fill DB");
		debugMenuItem.add(dbgFill);
		dbgFill.addActionListener(new FillDbEvent());

		final JMenuItem dbgClear = new JMenuItem("Clear DB");
		debugMenuItem.add(dbgClear);
		dbgClear.addActionListener(new ClearDbEvent());

		return debugMenuItem;
	}

	/**
	 * Gets the (current) instance.
	 * @return the instance of this window
	 */
	public static MainGUI getInstance() {
		if (instance == null) {
			throw new IllegalStateException("The MainGUI should be initialized before trying to get the instance");
		}
		return instance;
	}

	/**
	 * Create and show the frame.
	 */
	public static void createAndShowGUI() {
		//Set the look and feel.
		try{
			// Using OpenJDK, there is a bug that causes the
			// application to freeze when using a GTK look and feel.
			final boolean crossplatform = Boolean.getBoolean("beaform.useCrossPlatformLookAndFeel");
			if (crossplatform) {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
			else {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("Setting look and feel failed", e);
			}
		}

		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		//Create and set up the window.
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final MainGUI app = new MainGUI();
		app.init();
		frm.add(app.getPanel());
		frm.setLocation(150, 150);

		//Display the window.
		//frm.pack();
		frm.setSize(700, 500);
		frm.setVisible(true);

		instance = app;
	}

	/**
	 * This action lets one launch the 'about' window.
	 *
	 * @author steven
	 *
	 */
	public static final class AboutLaunchAction implements ActionListener {

		/**
		 * Invoked when the action is triggered.
		 *
		 * @param event the event object
		 */
		@Override
		public void actionPerformed(final ActionEvent event){
			javax.swing.SwingUtilities.invokeLater(new About());
		}
	}

}