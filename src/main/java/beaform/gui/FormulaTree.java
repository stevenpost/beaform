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

import beaform.Ingredient;
import beaform.entities.Formula;
import beaform.gui.formulaeditor.FormulaEditor;

public class FormulaTree extends JPanel implements TreeSelectionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 2506532995127262817L;

	private final JTree tree;
	private final JEditorPane htmlPane;

	private static boolean playWithLineStyle;
	private static String lineStyle = "Horizontal";

	public FormulaTree(final Formula formula) {
		super(new GridLayout(1,0));

		final DefaultMutableTreeNode top = new DefaultMutableTreeNode(new TreeViewFormula(formula));
		createNodes(top);

		//Create a tree that allows one selection at a time.
		this.tree = new JTree(top);
		this.tree.setToggleClickCount(0);
		this.tree.getSelectionModel().setSelectionMode
		(TreeSelectionModel.SINGLE_TREE_SELECTION);

		//Listen for when the selection changes.
		this.tree.addTreeSelectionListener(this);

		//Listen for double click events.
		this.tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {
				if(e.getClickCount() == 2) { // NOPMD by steven on 5/16/16 3:59 PM
					doubleClick();
				}
			}
		});

		if (playWithLineStyle) {
			System.out.println("line style = " + lineStyle);
			this.tree.putClientProperty("JTree.lineStyle", lineStyle);
		}

		//Create the scroll pane and add the tree to it.
		final JScrollPane treeView = new JScrollPane(this.tree);

		//Create the HTML viewing pane.
		this.htmlPane = new JEditorPane();
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

	private static void createNodes(DefaultMutableTreeNode top) { // NOPMD by steven on 5/16/16 3:58 PM

		final TreeViewFormula form = (TreeViewFormula) top.getUserObject();
		final List<Ingredient> ingredients = form.getFormula().getIngredients();
		for (final Ingredient ingredient : ingredients) {
			top.add(new DefaultMutableTreeNode(new TreeViewFormula(ingredient))); // NOPMD by steven on 5/16/16 3:58 PM
		}
	}

	@Override
	public void valueChanged(final TreeSelectionEvent e) {
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
		final StringBuilder description = new StringBuilder(31);
		if (node.isLeaf()) {
			description.append("Amount: ").append(form.getAmount()).append("\n\n");
		}
		else {
			description.append("Total amount: ").append(form.getFormula().getTotalAmount()).append("\n\n");
		}
		description.append("Descrtiption:\n").append(form.getFormula().getDescription()).append("\n\nTags: \n").append(String.join(",", form.getFormula().getTagsAsStrings()));
		this.htmlPane.setText(description.toString());
	}

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

}
