package beaform.gui.formulaeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.apache.commons.collections.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.entities.Formula;
import beaform.entities.FormulaDAO;
import beaform.entities.FormulaTag;
import beaform.entities.Ingredient;

/**
 * This class represents a GUI for editing formulas.
 *
 * @author Steven Post
 *
 */
public class FormulaEditor extends JPanel {

	/** A serial */
	private static final long serialVersionUID = 2557014310487638917L;

	/** A logger */
	private static final Logger LOG = LoggerFactory.getLogger(FormulaEditor.class);

	/** A text field for the name of the formula */
	private final JTextField txtName = new JTextField();

	/** A text field for the description of a formula */
	private final JTextArea txtDescription = new JTextArea();

	/** A text field for the total amount of a formula */
	private final JTextField txtTotalAmount = new JTextField();

	/** The 'save' button */
	private final JButton btnSubmit = new JButton("Submit");

	/** The panel with all the tag components */
	private final TagPane tagPane = new TagPane();

	/** The panel with all the ingredient components */
	private final IngredientPane ingredientPane = new IngredientPane();

	/** The formula that needs editing */
	private Formula formula;

	/**
	 * Main constructor for this editor to add a new formula.
	 * If you want to edit an existing one,
	 * use the overridden constructor that takes a formula as argument.
	 */
	public FormulaEditor() {
		super();
		init();

		this.btnSubmit.addActionListener(new AddNewFormulaTrigger());
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

		this.btnSubmit.addActionListener(new UpdateFormulaTrigger());

	}

	private void fillEditorFromFormula(final Formula formula) {
		this.formula = formula;
		this.txtName.setText(formula.getName());
		this.txtDescription.setText(formula.getDescription());
		this.txtTotalAmount.setText(formula.getTotalAmount());

		try {
			final List<Ingredient> ingredientList = new FormulaDAO().getIngredients(formula);
			this.ingredientPane.addIngredients(ingredientList);

			// Add tags to the list
			final Iterator<FormulaTag> tagIterator = formula.getTags();
			this.tagPane.addMultipleTags(tagIterator);
		}
		catch (NotSupportedException | SystemException e) {
			LOG.error("Failed to add all tags and ingredients", e);
		}
	}

	/**
	 * Initialize the formula editor.
	 *
	 * Note that this will <strong>not</strong> add the needed behaviour
	 * to the submit button.
	 */
	private void init() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		addGeneralComponentsToPanel(this);

		this.add(this.ingredientPane);
		this.add(this.tagPane);
		this.add(this.btnSubmit);
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

	private void createTotalAmountComponents(final JPanel parent, final JTextField totalAmount, final Dimension textFieldSize,
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

	private void createNameComponents(final JPanel parent, final Dimension textFieldSize, final Dimension textFieldMaxSize) {
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

	private void createDescriptionComponents(final JPanel parent, final JTextArea description) {
		final Dimension textAreaSize = new Dimension(100, 90);
		final Dimension textAreaMaxSize = new Dimension(500, 400);
		final JLabel descriptionLabel = new JLabel("Description");
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(descriptionLabel);

		description.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		description.setMinimumSize(textAreaSize);
		description.setPreferredSize(textAreaSize);
		description.setMaximumSize(textAreaMaxSize);
		panel.add(description);

		parent.add(panel);
	}

	/**
	 * Invoked when the action occurs.
	 *
	 */
	public void addNewFormula() {
		final String name = this.txtName.getText();
		final String description = this.txtDescription.getText();
		final String totalAmount = this.txtTotalAmount.getText();

		if (LOG.isInfoEnabled()) {
			LOG.info("Add: " + name + " with description: " + description);
		}

		final List<Ingredient> ingredients = getIngredientList();
		final List<FormulaTag> tags = getTagList();

		try {
			new FormulaDAO().addFormula(name, description, totalAmount, ingredients, tags);
		}
		catch (SystemException | NotSupportedException e1) {
			LOG.error("Something wen wrong adding the new formula", e1);
		}

	}

	@SuppressWarnings("unchecked")
	private List<Ingredient> getIngredientList() {
		final Iterator<Ingredient> ingredients = this.ingredientPane.getIngredients();
		return IteratorUtils.toList(ingredients);
	}

	@SuppressWarnings("unchecked")
	private List<FormulaTag> getTagList() {
		final Iterator<FormulaTag> tags = this.tagPane.getTags();
		return IteratorUtils.toList(tags);
	}

	/**
	 * Updates an existing formula.
	 */
	public void updateFormula() {
		if (LOG.isInfoEnabled()) {
			final String name = this.formula.getName();
			final String description = this.txtDescription.getText();
			LOG.info("Edit: " + name + " with description: " + description);
		}

		final List<Ingredient> ingredients = getIngredientList();
		final List<FormulaTag> tags = getTagList();

		try {
			final String name = this.formula.getName();
			final String description = this.txtDescription.getText();
			final String totalAmount = this.txtTotalAmount.getText();
			new FormulaDAO().updateExisting(name, description, totalAmount, ingredients, tags);
		}
		catch (SystemException | NotSupportedException e1) {
			LOG.error("Something went wrong updating the formula", e1);
		}

	}

	/**
	 * An trigger for updating an existing formula.
	 *
	 * @author Steven Post
	 *
	 */
	final class UpdateFormulaTrigger implements ActionListener {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(final ActionEvent event) {
			updateFormula();
		}
	}

	/**
	 * An trigger for adding of a new formula.
	 *
	 * @author Steven Post
	 *
	 */
	final class AddNewFormulaTrigger implements ActionListener {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(final ActionEvent event) {
			addNewFormula();
		}
	}

}
