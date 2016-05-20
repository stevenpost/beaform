package beaform.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.gui.formulaeditor.FormulaEditor;

/**
 * An action to show a clean formula editor, in order to create a new formula.
 *
 * @author Steven Post
 *
 */
public class NewAddWindowAction implements ActionListener {

	/** Panel for the new content */
	private final MainPanel panel;

	/**
	 * Constructor
	 *
	 * @param panel the target panel
	 */
	public NewAddWindowAction(final MainPanel panel) {
		this.panel = panel;
	}

	/**
	 * Invoked when the action occurs.
	 *
	 * @param event the event object
	 */
	@Override
	public void actionPerformed(final ActionEvent event) {
		this.panel.replaceWindow(new FormulaEditor());
	}

}
