package beaform.gui;

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

	/** Logger */
	private static final Logger LOG = LoggerFactory.getLogger(About.class);

	/** License text */
	private static final String LICENSE = "This program is distributed under the MIT License.\n" +
					"You can re-distribute, and/or modify the program under the conditions of the MIT License.";

	/** Border size */
	private static final int BORDERSIZE = 20;

	/** The main frame of this dialog */
	private static JFrame frm = new JFrame("About...");

	/** The main content panel of this dialog */
	private static JPanel pane = new JPanel();

	/** The title label */
	private static JLabel lblTitle = new JLabel();

	/** The author label */
	private static JLabel lblAuthor = new JLabel();

	/** The area in which to display the license text */
	private static JTextArea txtLicense = new JTextArea();

	/**
	 * Runs the body of this task.
	 * This should not be invoked directly.
	 */
	@Override
	public void run() {
		createAndShowGUI();
	}

	private void init(){

		//Create pane and add components
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

		lblTitle.setText("BeaForm");
		pane.add(lblTitle);

		lblAuthor.setText("By Steven Post");
		pane.add(lblAuthor);

		txtLicense.setText(LICENSE);
		txtLicense.setEditable(false);
		pane.add(txtLicense);

		pane.setBorder(BorderFactory.createEmptyBorder(BORDERSIZE, BORDERSIZE, BORDERSIZE, BORDERSIZE));

	}

	/**
	 * Create and show the about box.
	 */
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
		frm.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		final About app = new About();
		app.init();
		final Component contents = pane;
		frm.getContentPane().add(contents, BorderLayout.CENTER);
		frm.setLocation(150, 150);

		//Display the window.
		frm.setMinimumSize(new Dimension(200, 100));
		frm.pack();
		frm.setVisible(true);
	}
}
