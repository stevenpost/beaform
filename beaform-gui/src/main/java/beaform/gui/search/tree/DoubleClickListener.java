package beaform.gui.search.tree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A listener that activates on double clicks.
 *
 * @author Steven Post
 *
 */
public class DoubleClickListener extends MouseAdapter {

	/** The number of clicks to register a double click on the mouse */
	private static final int DOUBLE_CLICK = 2;

	private final FormulaTree formulaTree;

	/**
	 * @param formulaTree
	 */
	public DoubleClickListener(FormulaTree formulaTree) {
		this.formulaTree = formulaTree;
	}

	/**
	 * This method fires when the mouse is clicked,
	 * but only does something on double click events.
	 *
	 * @param event The event passed to this method when clicks are seen.
	 */
	@Override
	public void mousePressed(final MouseEvent event) {
		if(event.getClickCount() == DOUBLE_CLICK) {
			this.formulaTree.editSelectedFormula();
		}
	}
}
