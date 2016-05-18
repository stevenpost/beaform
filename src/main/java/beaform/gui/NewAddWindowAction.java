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

	/**
	 * Invoked when the action occurs.
	 *
	 * @param event the event object
	 */
	@Override
	public void actionPerformed(final ActionEvent event) {
		MainGUI.getInstance().replaceWindow(new FormulaEditor());
	}

}
