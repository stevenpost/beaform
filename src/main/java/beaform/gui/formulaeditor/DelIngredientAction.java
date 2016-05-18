package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import beaform.entities.Ingredient;

/**
 * This action removes an ingredient from the list of ingredients.
 *
 * @author Steven Post
 *
 */
public final class DelIngredientAction implements ActionListener {

	/**
	 * The list model for ingredients.
	 */
	private final DefaultListModel<Ingredient> lstFormulaModel;

	/** The graphical list */
	private final JList<Ingredient> lstFormulas;

	/**
	 * Constructor
	 *
	 * @param lstFormulas The graphical list.
	 * @param lstFormulaModel The list model for ingredients.
	 */
	public DelIngredientAction(final JList<Ingredient> lstFormulas, final DefaultListModel<Ingredient> lstFormulaModel) {
		this.lstFormulaModel = lstFormulaModel;
		this.lstFormulas = lstFormulas;
	}

	/**
	 * This method is invoked when the event is triggered.
	 *
	 * @param event The event object
	 */
	@Override
	public void actionPerformed(final ActionEvent event) {
		while (!this.lstFormulas.isSelectionEmpty()) {
			final int selected = this.lstFormulas.getSelectedIndex();
			this.lstFormulaModel.remove(selected);
		}
	}
}