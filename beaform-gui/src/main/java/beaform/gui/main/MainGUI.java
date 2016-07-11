package beaform.gui.main;

import java.awt.Component;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.gui.InterchangableWindow;


/**
 * This class represents the main user interface.
 * @author Steven Post
 *
 */
public final class MainGUI implements InterchangableWindowDisplayer {

	private static final Logger LOG = LoggerFactory.getLogger(MainGUI.class);

	private static final int WINDOW_WIDTH = 700;
	private static final int WINDOW_HEIGHT= 500;
	private static final int WINDOW_X_LOCATION = 150;
	private static final int WINDOW_Y_LOCATION = 150;
	private static volatile MainGUI instance;
	private static JFrame mainFrame = new JFrame("BeaForm");
	private final MainPanel contentPanel = new MainPanel();

	private MainGUI() {
		init();
	}

	private void init(){
		final JMenuBar menu = createMenu();
		mainFrame.setJMenuBar(menu);

		LOG.debug("end init");
	}

	@Override
	public void replaceActiveWindow(final Component comp) {
		this.contentPanel.replaceActiveWindow(comp);
	}

	private JPanel getPanel() {
		return this.contentPanel;
	}

	private JMenuBar createMenu() {
		final JMenuBar menu = new JMenuBar();

		SubMenu newmenu = new NewMenu();
		newmenu.attachToMenuBar(menu);

		SubMenu helpmenu = new HelpMenu();
		helpmenu.attachToMenuBar(menu);

		SubMenu debugMenu = new DebugMenu(this.contentPanel);
		debugMenu.attachToMenuBar(menu);

		return menu;
	}

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
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final MainGUI app = new MainGUI();
		mainFrame.add(app.getPanel());
		mainFrame.setLocation(WINDOW_X_LOCATION, WINDOW_Y_LOCATION);

		//Display the window.
		mainFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		mainFrame.setVisible(true);

		instance = app;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Component) {
			replaceActiveWindow((Component) arg);
		}
		else if (arg instanceof InterchangableWindow) {
			InterchangableWindow icw = (InterchangableWindow) arg;
			icw.replace();
		}
	}

}
