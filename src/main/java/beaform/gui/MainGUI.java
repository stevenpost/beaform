package beaform.gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;

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

	private static MainGUI instance;
	private static final Logger LOG = LoggerFactory.getLogger(MainGUI.class);

	private static JFrame frm = new JFrame("BeaForm");
	private final transient JMenuBar menu = new JMenuBar();
	private final transient GridLayout layout = new GridLayout();
	private final transient JPanel panel = new JPanel(this.layout);

	private void init(){

		//Create pane and add components
		createMenu(this.menu);

		//Putting components in place
		frm.setJMenuBar(this.menu);

		LOG.info("end init");
	}

	/**
	 * This method will replace the current active window.
	 *
	 * @param comp The new window to display
	 */
	public void replaceWindow(final Component comp) {
		if (this.panel.getComponentCount() > 0) {
			this.panel.remove(0);
		}
		this.panel.add(comp);
		this.panel.revalidate();
	}

	private JPanel getPanel() {
		return this.panel;
	}

	private void createMenu(final JMenuBar menu) {
		menu.add(createNewMenu());
		menu.add(createHelpMenu());
		menu.add(createDebugMenu());
	}

	private JMenu createNewMenu() {
		final JMenu newMenuItem = new JMenu("New...");

		final JMenuItem search = new JMenuItem("Search");
		newMenuItem.add(search);
		search.addActionListener(new NewSearchWindowAction());

		final JMenuItem add = new JMenuItem("Add");
		newMenuItem.add(add);
		add.addActionListener(new NewAddWindowAction());

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

	public static void createAndShowGUI() {
		//Set the look and feel.
		try{
			// Using OpenJDK, there is a bug that causes the
			// application to freeze when using a GTK look and feel.
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch (Exception e){
			if (LOG.isErrorEnabled()) {
				LOG.error("Look'n feel: " + e.getMessage());
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
		frm.setSize(600, 400);
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

		@Override
		public void actionPerformed(final ActionEvent event){
			javax.swing.SwingUtilities.invokeLater(new About());
		}
	}

}