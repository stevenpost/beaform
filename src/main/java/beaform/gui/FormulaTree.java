package beaform.gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class FormulaTree extends JPanel implements TreeSelectionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 2506532995127262817L;

	private final JTree tree;
	private final JEditorPane htmlPane;

	private static boolean playWithLineStyle = false;
	private static String lineStyle = "Horizontal";

	public FormulaTree() {
		super(new GridLayout(1,0));

		DefaultMutableTreeNode top = new DefaultMutableTreeNode("The Java Series");
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

	private void createNodes(DefaultMutableTreeNode top) {
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode book = null;

		category = new DefaultMutableTreeNode("Books for Java Programmers");
		top.add(category);

		//original Tutorial
		book = new DefaultMutableTreeNode(new String
		                                  ("The Java Tutorial: A Short Course on the Basics"));
		category.add(book);

		//Tutorial Continued
		book = new DefaultMutableTreeNode(new String
		                                  ("The Java Tutorial Continued: The Rest of the JDK"));
		category.add(book);

		//Swing Tutorial
		book = new DefaultMutableTreeNode(new String
		                                  ("The Swing Tutorial: A Guide to Constructing GUIs"));
		category.add(book);

		//...add more books for programmers...

		category = new DefaultMutableTreeNode("Books for Java Implementers");
		top.add(category);

		//VM
		book = new DefaultMutableTreeNode(new String
		                                  ("The Java Virtual Machine Specification"));
		category.add(book);

		//Language Spec
		book = new DefaultMutableTreeNode(new String
		                                  ("The Java Language Specification"));
		category.add(book);

	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub

	}

}
