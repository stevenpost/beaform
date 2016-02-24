package beaform.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.neo4j.graphdb.GraphDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.gui.search.NewSearchWindowAction;


/**
 * This class represents the main user interface.
 * @author Steven
 *
 */
public class MainGUI {
	private static Logger log = LoggerFactory.getLogger(MainGUI.class);

	private static JFrame frm = new JFrame("BeaForm");
	private static JMenuBar menu = new JMenuBar();
	private final GridLayout layout = new GridLayout();
	private final JPanel panel = new JPanel(this.layout);
	private final GraphDatabaseService graphDb;

	public MainGUI(GraphDatabaseService graphDb) {
		this.graphDb = graphDb;
	}

	private void init(){

		//Create pane and add components

		JMenu newMenuItem = new JMenu("New...");
		menu.add(newMenuItem);

		JMenuItem formview = new JMenuItem("TreeView");
		newMenuItem.add(formview);
		formview.addActionListener(new NewFormViewAction(this.panel));

		JMenuItem search = new JMenuItem("Search");
		newMenuItem.add(search);
		search.addActionListener(new NewSearchWindowAction(this.panel, this.graphDb));

		JMenu helpmenu = new JMenu("Help");
		menu.add(helpmenu);

		JMenuItem about = new JMenuItem("About...");
		helpmenu.add(about);
		about.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				javax.swing.SwingUtilities.invokeLater(new About());
			}
		});

		//Putting components in place
		frm.setJMenuBar(menu);

		log.info("end init");
	}

	private JPanel getPanel() {
		return this.panel;
	}

	public static void createAndShowGUI(GraphDatabaseService graphDb) {
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

		MainGUI app = new MainGUI(graphDb);
		app.init();
		frm.add(app.getPanel());
		frm.setLocation(150, 150);

		//Display the window.
		//frm.pack();
		frm.setSize(600, 400);
		frm.setVisible(true);
	}

}