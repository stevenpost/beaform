package beaform.gui.formulaeditor;

import java.util.List;
import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.FormulaDAO;
import beaform.entities.Formula;
import beaform.entities.FormulaTag;
import beaform.entities.Ingredient;
import beaform.gui.subwindows.InterchangableWindow;

/**
 * This class represents a GUI for editing formulas.
 *
 * @author Steven Post
 *
 */
public final class FormulaEditor extends Observable implements InterchangableWindow {

	private static final Logger LOG = LoggerFactory.getLogger(FormulaEditor.class);

	private final FormulaEditorUI editorUI;

	/**
	 * Main constructor for this editor to add a new formula.
	 * If you want to edit an existing one,
	 * use the overridden constructor that takes a formula as argument.
	 */
	public FormulaEditor() {
		this.editorUI = new FormulaEditorUI(this);
	}

	/**
	 * Constructor that makes this an editor for existing formulas.
	 *
	 * @param formula The formula that needs editing.
	 */
	public FormulaEditor(final Formula formula) {
		this.editorUI = new FormulaEditorUI(this, formula);
	}

	public void addNewFormula() {
		final String name = this.editorUI.getName();
		final String description = this.editorUI.getDescription();
		final String totalAmount = this.editorUI.getTotalAmount();

		if (LOG.isInfoEnabled()) {
			LOG.info("Add: " + name + " with description: " + description);
		}

		final List<Ingredient> ingredients = this.editorUI.getIngredientList();
		final List<FormulaTag> tags = this.editorUI.getTagList();

		FormulaDAO.addFormula(name, description, totalAmount, ingredients, tags);

	}

	public void updateFormula() {
		if (LOG.isInfoEnabled()) {
			final String name = this.editorUI.getName();
			final String description = this.editorUI.getDescription();
			LOG.info("Edit: " + name + " with description: " + description);
		}

		final List<Ingredient> ingredients = this.editorUI.getIngredientList();
		final List<FormulaTag> tags = this.editorUI.getTagList();

		final String name = this.editorUI.getName();
		final String description = this.editorUI.getDescription();
		final String totalAmount = this.editorUI.getTotalAmount();
		FormulaDAO.updateExisting(name, description, totalAmount, ingredients, tags);

	}

	@Override
	public void replace() {
		this.notifyObservers(this.editorUI.getPanel());
	}

}
