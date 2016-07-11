package beaform.gui.formulaeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.collections.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.FormulaDAO;
import beaform.entities.Formula;
import beaform.entities.FormulaTag;
import beaform.entities.Ingredient;
import beaform.gui.InterchangableWindow;
import beaform.gui.InterchangableWindowDisplayer;

/**
 * This class represents a GUI for editing formulas.
 *
 * @author Steven Post
 *
 */
public final class FormulaEditor implements InterchangableWindow {

	private static final Logger LOG = LoggerFactory.getLogger(FormulaEditor.class);

	private final InterchangableWindowDisplayer icwd;
	private final JPanel panel = new JPanel();
	private final JTextField txtName = new JTextField();
	private final JTextArea txtDescription = new JTextArea();
	private final JTextField txtTotalAmount = new JTextField();
	private final JButton btnSave = new JButton("Save");
	private final TagPane tagPane = new TagPane();
	private final IngredientPane ingredientPane = new IngredientPane();

	private Formula formula;

	/**
	 * Main constructor for this editor to add a new formula.
	 * If you want to edit an existing one,
	 * use the overridden constructor that takes a formula as argument.
	 * @param icwd the window displayer that controls this editor
	 */
	public FormulaEditor(final InterchangableWindowDisplayer icwd) {
		this.icwd = icwd;
		init();

		this.btnSave.addActionListener(event -> addNewFormula());
		this.replace();
	}

	/**
	 * Constructor that makes this an editor for existing formulas.
	 *
	 * @param icwd the window displayer that controls this editor
	 * @param formula The formula that needs editing.
	 */
	public FormulaEditor(final InterchangableWindowDisplayer icwd, final Formula formula) {
		this.icwd = icwd;
		init();

		// Don't allow editing the name of a formula since this
		// will cause duplicate entity errors when persisting.
		this.txtName.setEnabled(false);

		fillEditorFromFormula(formula);

		this.btnSave.addActionListener(event -> updateFormula());
		this.replace();
	}

	private void fillEditorFromFormula(final Formula newFormula) {
		this.formula = newFormula;
		this.txtName.setText(newFormula.getName());
		this.txtDescription.setText(newFormula.getDescription());
		this.txtTotalAmount.setText(newFormula.getTotalAmount());

		final List<Ingredient> ingredientList = FormulaDAO.getIngredients(newFormula);
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
	}

	private void addGeneralComponentsToPanel(final JPanel parent) {
		final JPanel genralComponents = new JPanel();
		genralComponents.setLayout(new BoxLayout(genralComponents, BoxLayout.X_AXIS));
		final Dimension textFieldSize = new Dimension(100, 30);
		final Dimension textFieldMaxSize = new Dimension(200, 30);
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

	private void addNewFormula() {
		final String name = this.txtName.getText();
		final String description = this.txtDescription.getText();
		final String totalAmount = this.txtTotalAmount.getText();

		if (LOG.isInfoEnabled()) {
			LOG.info("Add: " + name + " with description: " + description);
		}

		final List<Ingredient> ingredients = getIngredientList();
		final List<FormulaTag> tags = getTagList();

		FormulaDAO.addFormula(name, description, totalAmount, ingredients, tags);

	}

	private List<Ingredient> getIngredientList() {
		final Iterator<Ingredient> ingredients = this.ingredientPane.getIngredients();
		return IteratorUtils.toList(ingredients);
	}

	private List<FormulaTag> getTagList() {
		final Iterator<FormulaTag> tags = this.tagPane.getTags();
		return IteratorUtils.toList(tags);
	}

	private void updateFormula() {
		if (LOG.isInfoEnabled()) {
			final String name = this.formula.getName();
			final String description = this.txtDescription.getText();
			LOG.info("Edit: " + name + " with description: " + description);
		}

		final List<Ingredient> ingredients = getIngredientList();
		final List<FormulaTag> tags = getTagList();

		final String name = this.formula.getName();
		final String description = this.txtDescription.getText();
		final String totalAmount = this.txtTotalAmount.getText();
		FormulaDAO.updateExisting(name, description, totalAmount, ingredients, tags);

	}

	@Override
	public void replace() {
		this.icwd.replaceActiveWindow(this.panel);
	}

}
