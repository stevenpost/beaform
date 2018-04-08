package beaform.gui.formulaeditor;

import java.util.Collection;
import java.util.Observable;

import beaform.commands.Command;
import beaform.commands.CommandExecutor;
import beaform.commands.CreateNewFormulaCommand;
import beaform.commands.UpdateFormulaCommand;
import beaform.entities.Formula;
import beaform.entities.FormulaTag;
import beaform.entities.Ingredient;
import beaform.gui.subwindows.InterchangableWindow;
import beaform.utilities.ErrorDisplay;

/**
 * This class represents a GUI for editing formulas.
 *
 * @author Steven Post
 *
 */
public final class FormulaEditor extends Observable implements InterchangableWindow, ErrorDisplay {

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
		if (!isUiFormulaValid()) {
			displayError("The name of a formula can not be empty");
			return;
		}
		Formula formula = buildFormulaFromUI();

		Command createCommand = new CreateNewFormulaCommand(formula, this);
		CommandExecutor executor = CommandExecutor.getInstance();
		executor.execute(createCommand);
	}

	public void updateFormula() {
		if (!isUiFormulaValid()) {
			displayError("The name of a formula can not be empty");
			return;
		}
		Formula updatedFormula = buildFormulaFromUI();

		Command updateCommand = new UpdateFormulaCommand(updatedFormula, this);
		CommandExecutor executor = CommandExecutor.getInstance();
		executor.execute(updateCommand);
	}

	private boolean isUiFormulaValid() {
		if ("".equals(this.editorUI.getName())) {
			return false;
		}
		return true;
	}

	private Formula buildFormulaFromUI() {
		final String name = this.editorUI.getName();
		final String description = this.editorUI.getDescription();
		final String totalAmount = this.editorUI.getTotalAmount();
		Formula formula = new Formula(name, description, totalAmount);

		final Collection<Ingredient> ingredients = this.editorUI.getIngredientList();
		formula.addAllIngredients(ingredients);

		final Collection<FormulaTag> tags = this.editorUI.getTagList();
		formula.addAllTags(tags);

		return formula;
	}

	@Override
	public void replace() {
		this.setChanged();
		this.notifyObservers(this.editorUI.getPanel());
	}

	@Override
	public void displayError(String error) {
		this.editorUI.setError(error);
	}

}
