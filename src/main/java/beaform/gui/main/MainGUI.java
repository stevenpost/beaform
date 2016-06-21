package beaform.gui.main;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.gui.MainPanel;


/**
 * This class represents the main user interface.
 * @author Steven
 *
 */
public final class MainGUI {

	/** A logger */
	private static final Logger LOG = LoggerFactory.getLogger(MainGUI.class);

	/** Window width */
	private static final int WINDOW_WIDTH = 700;

	/** Window height */
	private static final int WINDOW_HEIGHT= 500;

	/** Window X location */
	private static final int WINDOW_X_LOCATION = 150;

	/** Window Y location */
	private static final int WINDOW_Y_LOCATION = 150;

	/** The main instance of the GUI */
	private static volatile MainGUI instance;

	/** The main frame (or window) */
	private static JFrame frm = new JFrame("BeaForm");

	/** The panel containing all content of this window */
	private final MainPanel panel = new MainPanel();

	private MainGUI() {
		init();
	}

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
		menu.add(new NewMenu(this.panel));
		menu.add(new HelpMenu());
		menu.add(new DebugMenu(this.panel));

		return menu;
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
		frm.add(app.getPanel());
		frm.setLocation(WINDOW_X_LOCATION, WINDOW_Y_LOCATION);

		//Display the window.
		frm.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		frm.setVisible(true);

		instance = app;
	}

}
