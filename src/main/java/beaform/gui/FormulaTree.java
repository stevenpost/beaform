package beaform.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import beaform.entities.Formula;

public class FormulaTree extends JPanel implements TreeSelectionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 2506532995127262817L;

	private final JTree tree;
	private final JEditorPane htmlPane;

	private static boolean playWithLineStyle = false;
	private static String lineStyle = "Horizontal";

	public FormulaTree(Formula formula) {
		super(new GridLayout(1,0));

		DefaultMutableTreeNode top = new DefaultMutableTreeNode(formula);
		createNodes(top);

		//Create a tree that allows one selection at a time.
		this.tree = new JTree(top);
		this.tree.getSelectionModel().setSelectionMode
		(TreeSelectionModel.SINGLE_TREE_SELECTION);

		//Listen for when the selection changes.
		this.tree.addTreeSelectionListener(this);

		if (playWithLineStyle) {
			System.out.println("line style = " + lineStyle);
			this.tree.putClientProperty("JTree.lineStyle", lineStyle);
		}

		//Create the scroll pane and add the tree to it.
		JScrollPane treeView = new JScrollPane(this.tree);

		//Create the HTML viewing pane.
		this.htmlPane = new JEditorPane();
		this.htmlPane.setEditable(false);
		JScrollPane htmlView = new JScrollPane(this.htmlPane);

		//Add the scroll panes to a split pane.
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeView);
		splitPane.setBottomComponent(htmlView);

		Dimension minimumSize = new Dimension(100, 50);
		htmlView.setMinimumSize(minimumSize);
		treeView.setMinimumSize(minimumSize);
		splitPane.setDividerLocation(100);
		splitPane.setPreferredSize(new Dimension(500, 300));

		//Add the split pane to this panel.
		add(splitPane);
	}

	private static void createNodes(DefaultMutableTreeNode top) {

		Formula form = (Formula) top.getUserObject();
		Iterator<Entry<String, Formula>> it = form.getIngredients();
		while (it.hasNext()) {
			Entry<String, Formula> entry = it.next();
			top.add(new DefaultMutableTreeNode(new TreeViewFormula(entry.getValue(), entry.getKey())));
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		//Returns the last path element of the selection.
		//This method is useful only when the selection model allows a single selection.
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
						this.tree.getLastSelectedPathComponent();

		if (node == null) {
			//Nothing is selected.
			return;
		}

		Object nodeInfo = node.getUserObject();
		if (node.isLeaf()) {
			TreeViewFormula form = (TreeViewFormula)nodeInfo;
			StringBuilder sb = new StringBuilder();
			sb.append("Amount: ");
			sb.append(form.getMetadata());
			sb.append('\n');
			sb.append('\n');
			sb.append("Descrtiption:");
			sb.append('\n');
			sb.append(form.getFormula().getDescription());
			this.htmlPane.setText(sb.toString());
		}
		else {
			this.htmlPane.setText("");
		}
	}

}
