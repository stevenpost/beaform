package beaform.gui.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements an about dialog.
 *
 * @author steven
 *
 */
public class About implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(About.class);

	private static final String LICENSE = "This program is distributed under the MIT License.\n" +
					"You can re-distribute, and/or modify the program under the conditions of the MIT License.";
	private static final int BORDERSIZE = 20;
	private static final int WINDOW_WIDTH = 200;
	private static final int WINDOW_HEIGHT= 100;
	private static final int WINDOW_X_LOCATION = 150;
	private static final int WINDOW_Y_LOCATION = 150;
	private static final JFrame MAIN_FRAME = new JFrame("About...");
	private static final JPanel MAIN_PANEL = new JPanel();
	private static final JLabel TITLE = new JLabel("BeaForm");
	private static final JLabel AUTHOR = new JLabel("By Steven Post");
	private static final JTextArea LICENCE_FIELD = new JTextArea(LICENSE);

	@Override
	public void run() {
		createAndShowGUI();
	}

	private static void init(){

		//Create pane and add components
		MAIN_PANEL.setLayout(new BoxLayout(MAIN_PANEL, BoxLayout.Y_AXIS));

		MAIN_PANEL.add(TITLE);
		MAIN_PANEL.add(AUTHOR);

		LICENCE_FIELD.setEditable(false);
		MAIN_PANEL.add(LICENCE_FIELD);

		MAIN_PANEL.setBorder(BorderFactory.createEmptyBorder(BORDERSIZE, BORDERSIZE, BORDERSIZE, BORDERSIZE));

	}

	public static void createAndShowGUI() {
		//Set the look and feel.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e){
			if (LOG.isErrorEnabled()) {
				LOG.error("Look'n feel: " + e.getMessage(), e);
			}
		}

		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		//Create and set up the window.
		MAIN_FRAME.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		About.init();
		final Component contents = MAIN_PANEL;
		MAIN_FRAME.getContentPane().add(contents, BorderLayout.CENTER);
		MAIN_FRAME.setLocation(WINDOW_X_LOCATION, WINDOW_Y_LOCATION);

		//Display the window.
		MAIN_FRAME.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		MAIN_FRAME.pack();
		MAIN_FRAME.setVisible(true);
	}
}
