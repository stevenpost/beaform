package beaform.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.entities.Formula;
import beaform.entities.FormulaDAO;
import beaform.entities.Ingredient;
import beaform.gui.formulaeditor.FormulaEditor;

/**
 * This class implements a panel to show a tree view of formulas.
 *
 * @author Steven Post
 *
 */
public class FormulaTree extends JPanel implements TreeSelectionListener {

	/** A serial used for serialization */
	private static final long serialVersionUID = 2506532995127262817L;

	/** A logger */
	private static final Logger LOG = LoggerFactory.getLogger(FormulaTree.class);

	/** The visual Tree object */
	private final JTree tree;

	/**
	 * A description pane to show a more detailed
	 * description of the selected object
	 */
	private final JEditorPane htmlPane;

	/**
	 * Constructor for the tree view.
	 *
	 * @param formula The formula that is the starting point of the tree
	 */
	public FormulaTree(final Formula formula) {
		super(new GridLayout(1,0));

		final DefaultMutableTreeNode top = new DefaultMutableTreeNode(new TreeViewFormula(formula));
		createNodes(top);

		this.htmlPane = new JEditorPane();
		this.tree = new JTree(top);
		init();
	}

	/**
	 * Constructor for the tree view.
	 *
	 * @param formula The formula that is the starting point of the tree
	 */
	public FormulaTree(final List<Formula> formulas) {
		super(new GridLayout(1,0));

		final DefaultMutableTreeNode top = new DefaultMutableTreeNode("Search results");
		for (Formula formula : formulas) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(new TreeViewFormula(formula));
			top.add(node);
			createNodes(node);
		}

		this.htmlPane = new JEditorPane();
		this.tree = new JTree(top);
		init();
	}

	/**
	 * Initializes the tree view gui.
	 */
	private void init() {
		//Create a tree that allows one selection at a time.
		this.tree.setToggleClickCount(0);
		this.tree.getSelectionModel().setSelectionMode
		(TreeSelectionModel.SINGLE_TREE_SELECTION);

		//Listen for when the selection changes.
		this.tree.addTreeSelectionListener(this);

		//Listen for double click events.
		this.tree.addMouseListener(new DoubleClickListener());

		//Create the scroll pane and add the tree to it.
		final JScrollPane treeView = new JScrollPane(this.tree);

		//Create the HTML viewing pane.
		this.htmlPane.setEditable(false);
		final JScrollPane htmlView = new JScrollPane(this.htmlPane);

		//Add the scroll panes to a split pane.
		final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeView);
		splitPane.setBottomComponent(htmlView);

		final Dimension minimumSize = new Dimension(100, 50);
		htmlView.setMinimumSize(minimumSize);
		treeView.setMinimumSize(minimumSize);
		splitPane.setDividerLocation(100);
		splitPane.setPreferredSize(new Dimension(500, 300));

		//Add the split pane to this panel.
		add(splitPane);
	}

	private static void createNodes(final DefaultMutableTreeNode top) {
		final FormulaDAO formulaDAO = new FormulaDAO();

		final TreeViewFormula form = (TreeViewFormula) top.getUserObject();
		try {
			final List<Ingredient> ingredients = formulaDAO.getIngredients(form.getFormula());
			for (final Ingredient ingredient : ingredients) {
				top.add(new DefaultMutableTreeNode(new TreeViewFormula(ingredient))); // NOPMD by steven on 5/16/16 3:58 PM
			}
		}
		catch (NotSupportedException e) {
			LOG.error("There is already a transaction going on in this thread", e);
		}
		catch (SystemException e) {
			LOG.error("There was a problem with the transaction service", e);
		}
	}

	/**
	 * Called when the selection in the tree view changes.
	 * It will display the details of the selected node.
	 */
	@Override
	public void valueChanged(final TreeSelectionEvent event) {
		//Returns the last path element of the selection.
		//This method is useful only when the selection model allows a single selection.
		final DefaultMutableTreeNode node = (DefaultMutableTreeNode)
						this.tree.getLastSelectedPathComponent();

		if (node == null) {
			//Nothing is selected.
			return;
		}
		if (node.isRoot()) {
			// The root node is selected, reflect that in the description field.
			this.htmlPane.setText("Search result");
			return;
		}

		final TreeViewFormula form = (TreeViewFormula)node.getUserObject();
		final StringBuilder description = new StringBuilder(31);
		if (node.isLeaf()) {
			final String amount = form.getAmount();
			description.append("Amount: ").append(amount).append("\n\n");
		}
		else {
			final String amount = form.getFormula().getTotalAmount();
			description.append("Total amount: ").append(amount).append("\n\n");
		}

		final String formDescription = form.getDescription();
		final String tagsAsString = String.join(",", form.getTagsAsStrings());
		description.append("Descrtiption:\n").append(formDescription).append("\n\nTags: \n").append(tagsAsString);
		this.htmlPane.setText(description.toString());
	}

	/**
	 * This method is executed when double clicking on a node.
	 *
	 * It opens the formula editor with the formula of the node.
	 */
	public void doubleClick() {
		//Returns the last path element of the selection.
		//This method is useful only when the selection model allows a single selection.
		final DefaultMutableTreeNode node = (DefaultMutableTreeNode)
						this.tree.getLastSelectedPathComponent();

		if (node == null) {
			//Nothing is selected.
			return;
		}

		final Object nodeInfo = node.getUserObject();
		final TreeViewFormula form = (TreeViewFormula)nodeInfo;
		MainGUI.getInstance().replaceWindow(new FormulaEditor(form.getFormula()));
	}

	/**
	 * A listener that activates on double clicks.
	 *
	 * @author Steven Post
	 *
	 */
	public class DoubleClickListener extends MouseAdapter {

		/**
		 * This method fires when the mouse is clicked,
		 * but only does something on double click events.
		 *
		 * @param event The event passed to this method when clicks are seen.
		 */
		@Override
		public void mousePressed(final MouseEvent event) {
			if(event.getClickCount() == 2) { // NOPMD by steven on 5/16/16 3:59 PM
				doubleClick();
			}
		}
	}

}
