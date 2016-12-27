package beaform.gui.search.tree;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.util.Set;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import beaform.dao.FormulaDAO;
import beaform.entities.Formula;
import beaform.entities.Ingredient;

/**
 * This class implements a panel to show a tree view of formulas.
 *
 * @author Steven Post
 *
 */
public class FormulaTreeUI {

	private static final int MIN_WIDTH = 100;
	private static final int MIN_HEIGHT = 50;
	private static final int PREF_WIDTH = 500;
	private static final int PREF_HEIGHT = 300;
	private static final int DIVIDER_LOCATION = 100;

	private final FormulaTree formulatree;
	private final JPanel panel = new JPanel(new GridLayout(1,0));
	private final JTree tree;
	private final JEditorPane descriptionPanel;

	public FormulaTreeUI(final FormulaTree formulatree, final Formula formula) {
		this.formulatree = formulatree;
		final DefaultMutableTreeNode top = new DefaultMutableTreeNode(new TreeViewFormula(formula));
		createNodes(top);

		this.descriptionPanel = new JEditorPane();
		this.tree = new JTree(top);
		init();
	}

	public FormulaTreeUI(final FormulaTree formulatree, final List<Formula> formulas) {
		this.formulatree = formulatree;
		final DefaultMutableTreeNode top = new DefaultMutableTreeNode("Search results");
		for (final Formula formula : formulas) {
			addDescendantNodes(top, formula);
		}

		this.descriptionPanel = new JEditorPane();
		this.tree = new JTree(top);
		init();
	}

	/**
	 * Add any descendant nodes to the given parent node.
	 *
	 * @param parent The node that will get additional descendants.
	 * @param formula The formula for the direct child.
	 */
	private static void addDescendantNodes(final DefaultMutableTreeNode parent, final Formula formula) {
		final DefaultMutableTreeNode node = new DefaultMutableTreeNode(new TreeViewFormula(formula));
		parent.add(node);
		createNodes(node);
	}

	private void init() {
		//Create a tree that allows one selection at a time.
		this.tree.setToggleClickCount(0);
		this.tree.getSelectionModel().setSelectionMode
		(TreeSelectionModel.SINGLE_TREE_SELECTION);

		//Listen for when the selection changes.
		this.tree.addTreeSelectionListener(new SelectedItemChangedAction(this));

		//Listen for double click events.
		this.tree.addMouseListener(new DoubleClickListener(this.formulatree));

		//Create the scroll pane and add the tree to it.
		final JScrollPane treeView = new JScrollPane(this.tree);

		//Create the HTML viewing pane.
		this.descriptionPanel.setEditable(false);
		final JScrollPane htmlView = new JScrollPane(this.descriptionPanel);

		//Add the scroll panes to a split pane.
		final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeView);
		splitPane.setBottomComponent(htmlView);

		final Dimension minimumSize = new Dimension(MIN_WIDTH, MIN_HEIGHT);
		htmlView.setMinimumSize(minimumSize);
		treeView.setMinimumSize(minimumSize);
		splitPane.setDividerLocation(DIVIDER_LOCATION);
		splitPane.setPreferredSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));

		//Add the split pane to this panel.
		this.panel.add(splitPane);
	}

	private static void createNodes(final DefaultMutableTreeNode parent) {
		final TreeViewFormula formula = (TreeViewFormula) parent.getUserObject();
		final Set<Ingredient> ingredients = FormulaDAO.listIngredients(formula.getFormula());
		for (final Ingredient ingredient : ingredients) {
			addIngredientNode(parent, ingredient);
		}
	}

	private static void addIngredientNode(final DefaultMutableTreeNode parent, final Ingredient ingredient) {
		final TreeViewFormula formula = new TreeViewFormula(ingredient);
		final DefaultMutableTreeNode node = new DefaultMutableTreeNode(formula);
		parent.add(node);
	}

	/**
	 * Fill in the description field based on the currently selected node.
	 *
	 * @param node The currently selected node.
	 */
	public void fillDescription(final DefaultMutableTreeNode node) {
		if (node.getUserObject() instanceof String) {
			// The root node is selected, reflect that in the description field.
			this.descriptionPanel.setText((String) node.getUserObject());
			return;
		}

		final TreeViewFormula formula = (TreeViewFormula)node.getUserObject();
		final StringBuilder description = new StringBuilder();

		if (node.isLeaf()) {
			appendAmount(formula, description);
		}
		else {
			appendTotalAmount(formula, description);
		}

		appendDescription(formula, description);
		appendTags(formula, description);
		this.descriptionPanel.setText(description.toString());
	}

	private static void appendTags(final TreeViewFormula formula, final StringBuilder description) {
		final String tagsAsString = String.join(",", formula.getTagsAsStrings());
		description.append("\n\nTags: \n").append(tagsAsString);
	}

	private static void appendDescription(final TreeViewFormula formula, final StringBuilder description) {
		final String formDescription = formula.getDescription();
		description.append("Descrtiption:\n").append(formDescription);
	}

	private static void appendTotalAmount(final TreeViewFormula formula, final StringBuilder description) {
		final String amount = formula.getTotalAmount();
		description.append("Total amount: ").append(amount).append("\n\n");
	}

	private static void appendAmount(final TreeViewFormula formula, final StringBuilder description) {
		final String amount = formula.getAmount();
		description.append("Amount: ").append(amount).append("\n\n");
	}

	/**
	 *
	 * @return null if nothing was selected
	 */
	public TreeViewFormula getSelectedFormula() {
		final DefaultMutableTreeNode node = getSelectedNode();

		if (node == null) {
			//Nothing is selected.
			return null;
		}

		return extractFormula(node);
	}

	/**
	 *
	 * @return null if nothing was selected
	 */
	public DefaultMutableTreeNode getSelectedNode() {
		//Returns the last path element of the selection.
		//This method is useful only when the selection model allows a single selection.
		return (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
	}

	private static TreeViewFormula extractFormula(final DefaultMutableTreeNode node) {
		return (TreeViewFormula)node.getUserObject();
	}

	public JPanel getPanel() {
		return this.panel;
	}

}
