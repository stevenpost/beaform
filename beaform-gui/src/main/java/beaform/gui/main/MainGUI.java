package beaform.gui.main;

import java.awt.Component;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.gui.subwindows.InterchangableWindow;


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

	/** A lock for accessing the instance */
	private static final Object INSTANCELOCK = new Object();

	private static volatile MainGUI instance;
	private final JFrame mainFrame = new JFrame("BeaForm");
	private final MainPanel contentPanel = new MainPanel();

	private MainGUI() {
		init();
	}

	private void init(){
		//Create and set up the window.
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.mainFrame.add(this.contentPanel);
		this.mainFrame.setLocation(WINDOW_X_LOCATION, WINDOW_Y_LOCATION);

		//Display the window.
		this.mainFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.mainFrame.setVisible(true);

		final JMenuBar menu = createMenu();
		this.mainFrame.setJMenuBar(menu);

		LOG.debug("end init");
	}

	@Override
	public void replaceActiveWindow(final Component comp) {
		this.contentPanel.replaceActiveWindow(comp);
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
		synchronized (INSTANCELOCK) {
			if (instance == null) {
				instance = new MainGUI();
			}
			return instance;
		}
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
