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

/**
 * This class represents a GUI for editing formulas.
 *
 * @author Steven Post
 *
 */
public class FormulaEditor extends JPanel {

	private static final long serialVersionUID = 2557014310487638917L;
	private static final Logger LOG = LoggerFactory.getLogger(FormulaEditor.class);

	private final JTextField txtName = new JTextField();
	private final JTextArea txtDescription = new JTextArea();
	private final JTextField txtTotalAmount = new JTextField();
	private final JButton btnSave = new JButton("Save");
	private final TagPane tagPane = new TagPane();
	private final IngredientPane ingredientPane = new IngredientPane();

	private transient Formula formula;

	/**
	 * Main constructor for this editor to add a new formula.
	 * If you want to edit an existing one,
	 * use the overridden constructor that takes a formula as argument.
	 */
	public FormulaEditor() {
		super();
		init();

		this.btnSave.addActionListener(event -> addNewFormula());
	}

	/**
	 * Constructor that makes this an editor for existing formulas.
	 *
	 * @param formula The formula that needs editing.
	 */
	public FormulaEditor(final Formula formula) {
		super();
		init();

		// Don't allow editing the name of a formula since this
		// will cause duplicate entity errors when persisting.
		this.txtName.setEnabled(false);

		fillEditorFromFormula(formula);

		this.btnSave.addActionListener(event -> updateFormula());

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
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		addGeneralComponentsToPanel(this);

		this.add(this.ingredientPane);
		this.add(this.tagPane);
		this.add(this.btnSave);
	}

	private void addGeneralComponentsToPanel(final JPanel parent) {
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		final Dimension textFieldSize = new Dimension(100, 30);
		final Dimension textFieldMaxSize = new Dimension(200, 30);
		final JTextField totalAmount = this.txtTotalAmount;
		final JTextArea description = this.txtDescription;

		createNameComponents(panel, textFieldSize, textFieldMaxSize);
		createTotalAmountComponents(panel, totalAmount, textFieldSize, textFieldMaxSize);
		createDescriptionComponents(panel, description);

		parent.add(panel);
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
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		final JLabel nameLabel = new JLabel("Name");
		panel.add(nameLabel);

		final JTextField name = this.txtName;
		name.setMinimumSize(textFieldSize);
		name.setPreferredSize(textFieldSize);
		name.setMaximumSize(textFieldMaxSize);
		panel.add(name);

		parent.add(panel);
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

	public void addNewFormula() {
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

	public void updateFormula() {
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

}
