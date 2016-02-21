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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

	private void init(){

		//Create pane and add components

		JMenu window = new JMenu("Window");
		menu.add(window);

		JMenuItem formview = new JMenuItem("New...");
		window.add(formview);
		formview.addActionListener(new NewFormViewAction(this.panel));

		JMenu helpmenu = new JMenu("Help");
		menu.add(helpmenu);

		JMenuItem about = new JMenuItem("About...");
		helpmenu.add(about);
		about.addActionListener(new ActionListener(){
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