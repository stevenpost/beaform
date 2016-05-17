package beaform.gui.formulaeditor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import beaform.entities.FormulaTag;

/**
 * Represents the GUI panel inside the editor to manage the tags.
 *
 * @author Steven Post
 *
 */
public class TagPane extends JPanel {

	/** Dimensions for most text fields */
	private static final Dimension DIM_TEXTFIELDS = new Dimension(100, 30);

	/** Dimensions for lists */
	private static final Dimension DIM_LISTS = new Dimension(200, 100);

	/** A label for the list of tags */
	private static final JLabel LBL_TAGS = new JLabel("Tags");

	/** A list of formula tags */
	private final transient List<FormulaTag> tags = new ArrayList<FormulaTag>();

	/** A list model to get the list of tags to the screen */
	private final transient DefaultListModel<FormulaTag> lstTagModel = new DefaultListModel<FormulaTag>();

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -6447838745127288741L;

	/**
	 * Constructor.
	 */
	public TagPane() {
		super(new GridBagLayout());
		init();
	}

	private void init() {
		final GridBagConstraints constraints = new GridBagConstraints();

		int gridy = 0;
		final JList<FormulaTag> lstTags = new JList<FormulaTag>(this.lstTagModel);
		final JTextField txtNewTag = new JTextField();
		final JButton btnAddTag = new JButton("Add Tag");
		final JButton btnDelTag = new JButton("Remove Tag");

		constraints.gridx = 0;
		constraints.gridy = gridy;
		constraints.gridwidth = 2;
		this.add(LBL_TAGS, constraints);
		constraints.gridwidth = 1;

		gridy++;
		constraints.gridx = 0;
		constraints.gridy = gridy;
		constraints.gridheight = 3;
		lstTags.setMinimumSize(DIM_LISTS);
		lstTags.setPreferredSize(DIM_LISTS);
		lstTags.setMaximumSize(DIM_LISTS);
		this.add(lstTags, constraints);

		constraints.gridx = 1;
		constraints.gridy = gridy;
		constraints.gridheight = 1;
		txtNewTag.setMinimumSize(DIM_TEXTFIELDS);
		txtNewTag.setPreferredSize(DIM_TEXTFIELDS);
		txtNewTag.setMaximumSize(DIM_TEXTFIELDS);
		this.add(txtNewTag, constraints);

		gridy++;
		constraints.gridx = 1;
		constraints.gridy = gridy;
		btnAddTag.addActionListener(new AddTagAction(txtNewTag, this));
		this.add(btnAddTag, constraints);

		gridy++;
		constraints.gridx = 1;
		constraints.gridy = gridy;
		btnDelTag.addActionListener(new DelTagAction(lstTags, this));
		this.add(btnDelTag, constraints);
	}

	/**
	 * This method adds a tag to the list of tags for this formula
	 *
	 * @param tag the tag to add
	 */
	public void addTag(final FormulaTag tag) {
		this.tags.add(tag);
		sortTags();
	}

	/**
	 * This method adds multiple tags to the list of tags.
	 * @param tags the tags to add
	 */
	public void addMultipleTags(final Iterator<FormulaTag> tags) {
		while (tags.hasNext()) {
			this.tags.add(tags.next());
		}
		sortTags();
	}

	/**
	 * This method sorts the tags alphabetically in the list.
	 * should the list model and the backing list be out of sync,
	 * this method will synchronize them.
	 */
	public final void sortTags() {
		this.tags.sort(new TagComparator());

		this.lstTagModel.clear();
		for (final FormulaTag tag : this.tags) {
			this.lstTagModel.addElement(tag);
		}
	}

	/**
	 * Remove a tag from the list.
	 * @param i the index of the tag to delete
	 */
	public void removeTag(final int index) {
		this.tags.remove(index);
		this.lstTagModel.remove(index);
	}

	/**
	 * Get all the tags from this pane.
	 *
	 * @return a list of tags
	 */
	public Iterator<FormulaTag> getTags() {
		return this.tags.iterator();
	}

}
