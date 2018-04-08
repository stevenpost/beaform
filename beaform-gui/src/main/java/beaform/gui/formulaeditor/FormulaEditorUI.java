package beaform.gui.formulaeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.commons.collections.IteratorUtils;

import beaform.dao.FormulaDAO;
import beaform.entities.Formula;
import beaform.entities.FormulaTag;
import beaform.entities.Ingredient;

/**
 * This class represents a GUI for editing formulas.
 *
 * @author Steven Post
 *
 */
public final class FormulaEditorUI {

	private final FormulaEditor editor;
	private final JPanel panel = new JPanel();
	private final JTextField txtName = new JTextField();
	private final JTextArea txtDescription = new JTextArea();
	private final JTextField txtTotalAmount = new JTextField();
	private final JButton btnSave = new JButton("Save");
	private final TagPane tagPane = new TagPane();
	private final IngredientPane ingredientPane = new IngredientPane();
	private final JTextArea txtErrors = new JTextArea();

	/**
	 * Main constructor for this editor to add a new formula.
	 * If you want to edit an existing one,
	 * use the overridden constructor that takes a formula as argument.
	 * @param editor The associated editor
	 */
	public FormulaEditorUI(final FormulaEditor editor) {
		this.editor = editor;
		init();

		this.btnSave.addActionListener(new AddNewFormulaAction(editor, editor));
	}

	/**
	 * Constructor that makes this an editor for existing formulas.
	 *
	 * @param editor The associated editor
	 * @param formula The formula that needs editing.
	 */
	public FormulaEditorUI(final FormulaEditor editor, final Formula formula) {
		this.editor = editor;
		init();

		// Don't allow editing the name of a formula since this
		// will cause duplicate entity errors when persisting.
		this.txtName.setEnabled(false);

		fillEditorFromFormula(formula);

		this.btnSave.addActionListener(new UpdateFormulaAction(this.editor, this.editor));
	}

	private void fillEditorFromFormula(final Formula newFormula) {
		this.txtName.setText(newFormula.getName());
		this.txtDescription.setText(newFormula.getDescription());
		this.txtTotalAmount.setText(newFormula.getTotalAmount());

		final Set<Ingredient> ingredientList = FormulaDAO.listIngredients(newFormula);
		this.ingredientPane.addIngredients(ingredientList);

		// Add tags to the list
		final Iterator<FormulaTag> tagIterator = newFormula.getTags();
		this.tagPane.addMultipleTags(tagIterator);
	}

	/**
	 * Initialize the formula editor.
	 *
	 * Note that this will <strong>not</strong> add the needed behaviour
	 * to the save button.
	 */
	private void init() {
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.PAGE_AXIS));

		addGeneralComponentsToPanel(this.panel);

		this.panel.add(this.ingredientPane);
		this.panel.add(this.tagPane);
		this.panel.add(this.btnSave);

		this.txtErrors.setEditable(false);
		this.panel.add(this.txtErrors);
	}

	private void addGeneralComponentsToPanel(final JPanel parent) {
		final JPanel genralComponents = new JPanel();
		genralComponents.setLayout(new BoxLayout(genralComponents, BoxLayout.X_AXIS));
		final Dimension textFieldSize = new Dimension(100, 35);
		final Dimension textFieldMaxSize = new Dimension(200, 35);
		final JTextField totalAmount = this.txtTotalAmount;
		final JTextArea description = this.txtDescription;

		createNameComponents(genralComponents, textFieldSize, textFieldMaxSize);
		createTotalAmountComponents(genralComponents, totalAmount, textFieldSize, textFieldMaxSize);
		createDescriptionComponents(genralComponents, description);

		parent.add(genralComponents);
	}

	private static void createTotalAmountComponents(final JPanel parent,
	                                                final JTextField totalAmount,
	                                                final Dimension textFieldSize,
	                                                final Dimension textFieldMaxSize) {
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		final JLabel totalAmountLabel = new JLabel("Total amount");

		panel.add(totalAmountLabel);

		totalAmount.setMinimumSize(textFieldSize);
		totalAmount.setPreferredSize(textFieldSize);
		totalAmount.setMaximumSize(textFieldMaxSize);
		panel.add(totalAmount);

		parent.add(panel);
	}

	private void createNameComponents(final JPanel parent,
	                                  final Dimension textFieldSize,
	                                  final Dimension textFieldMaxSize) {
		final JPanel nameComponents = new JPanel();
		nameComponents.setLayout(new BoxLayout(nameComponents, BoxLayout.Y_AXIS));

		final JLabel nameLabel = new JLabel("Name");
		nameComponents.add(nameLabel);

		final JTextField name = this.txtName;
		name.setMinimumSize(textFieldSize);
		name.setPreferredSize(textFieldSize);
		name.setMaximumSize(textFieldMaxSize);
		nameComponents.add(name);

		parent.add(nameComponents);
	}

	private static void createDescriptionComponents(final JPanel parent, final JTextArea description) {
		final Dimension textAreaSize = new Dimension(100, 90);
		final JLabel descriptionLabel = new JLabel("Description");
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(descriptionLabel);

		description.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		description.setMinimumSize(textAreaSize);
		description.setPreferredSize(textAreaSize);
		panel.add(description);

		parent.add(panel);
	}

	public List<Ingredient> getIngredientList() {
		final Iterator<Ingredient> ingredients = this.ingredientPane.getIngredients();
		return IteratorUtils.toList(ingredients);
	}

	public List<FormulaTag> getTagList() {
		final Iterator<FormulaTag> tags = this.tagPane.getTags();
		return IteratorUtils.toList(tags);
	}

	public JPanel getPanel() {
		return this.panel;
	}

	public String getName() {
		return this.txtName.getText();
	}

	public String getDescription() {
		return this.txtDescription.getText();
	}

	public String getTotalAmount() {
		return this.txtTotalAmount.getText();
	}

	public void setError(String message) {
		SwingUtilities.invokeLater(() -> FormulaEditorUI.this.txtErrors.setText(message));
	}

}
