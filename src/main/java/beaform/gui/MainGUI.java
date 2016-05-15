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

	private static Logger log = LoggerFactory.getLogger(MainGUI.class);

	private static JFrame frm = new JFrame("BeaForm");
	private final JMenuBar menu = new JMenuBar();
	private final GridLayout layout = new GridLayout();
	private final JPanel panel = new JPanel(this.layout);

	private void init(){

		//Create pane and add components
		createMenu(this.menu);

		//Putting components in place
		frm.setJMenuBar(this.menu);

		log.info("end init");
	}

	public void replaceWindow(Component comp) {
		this.panel.remove(0);
		this.panel.add(comp);
		this.panel.invalidate();
	}

	private JPanel getPanel() {
		return this.panel;
	}

	private void createMenu(JMenuBar menu) {
		menu.add(createNewMenu());
		menu.add(createHelpMenu());
		menu.add(createDebugMenu());
	}

	private JMenu createNewMenu() {
		JMenu newMenuItem = new JMenu("New...");

		JMenuItem search = new JMenuItem("Search");
		newMenuItem.add(search);
		search.addActionListener(new NewSearchWindowAction(this.panel));

		JMenuItem add = new JMenuItem("Add");
		newMenuItem.add(add);
		add.addActionListener(new NewAddWindowAction(this.panel));

		return newMenuItem;
	}

	private JMenu createHelpMenu() {
		JMenu helpmenu = new JMenu("Help");

		JMenuItem about = new JMenuItem("About...");
		helpmenu.add(about);
		about.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				javax.swing.SwingUtilities.invokeLater(new About());
			}
		});

		return helpmenu;
	}

	private JMenu createDebugMenu() {
		JMenu debugMenuItem = new JMenu("Debug");

		JMenuItem dbgAllFormulas = new JMenuItem("List all formulas");
		debugMenuItem.add(dbgAllFormulas);
		dbgAllFormulas.addActionListener(new ListFormulasEvent());

		JMenuItem dbgFill = new JMenuItem("Fill DB");
		debugMenuItem.add(dbgFill);
		dbgFill.addActionListener(new FillDbEvent());

		JMenuItem dbgClear = new JMenuItem("Clear DB");
		debugMenuItem.add(dbgClear);
		dbgClear.addActionListener(new ClearDbEvent());

		return debugMenuItem;
	}

	public static void createAndShowGUI() {
		//Set the look and feel.
		try{
			// Using OpenJDK, there is a bug that causes the application to freeze when using a GTK look and feel.
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch (Exception e){
			log.error("Look'n feel: " + e.getMessage());
		}

		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		//Create and set up the window.
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MainGUI app = new MainGUI();
		app.init();
		frm.add(app.getPanel());
		frm.setLocation(150, 150);

		//Display the window.
		//frm.pack();
		frm.setSize(600, 400);
		frm.setVisible(true);
	}

}