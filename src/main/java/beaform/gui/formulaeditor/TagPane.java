package beaform.gui.formulaeditor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import beaform.gui.Utilities;

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

	/** What text should be in the text field after adding the tag */
	private static final String AFTER_ADD = "";

	/** A list of formula tags */
	private final List<FormulaTag> tags = new ArrayList<FormulaTag>();

	/** A list model to get the list of tags to the screen */
	private final DefaultListModel<FormulaTag> lstTagModel = new DefaultListModel<FormulaTag>();

	/** The graphical list of tags */
	private final JList<FormulaTag> lstTags = new JList<FormulaTag>(this.lstTagModel);

	/** A field for the name of the new tag */
	private final JTextField txtNewTag = new JTextField();

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
		final JButton btnAddTag = new JButton("Add Tag");
		final JButton btnDelTag = new JButton("Remove Tag");

		constraints.gridx = 0;
		constraints.gridy = gridy;
		constraints.gridwidth = 2;
		Utilities.setBoldFont(LBL_TAGS);
		this.add(LBL_TAGS, constraints);
		constraints.gridwidth = 1;

		constraints.fill = GridBagConstraints.HORIZONTAL;

		gridy++;
		constraints.gridx = 0;
		constraints.gridy = gridy;
		constraints.gridheight = 3;
		this.lstTags.setMinimumSize(DIM_LISTS);
		this.lstTags.setPreferredSize(DIM_LISTS);
		this.lstTags.setMaximumSize(DIM_LISTS);
		this.add(this.lstTags, constraints);

		constraints.gridx = 1;
		constraints.gridy = gridy;
		constraints.gridheight = 1;
		this.txtNewTag.setMinimumSize(DIM_TEXTFIELDS);
		this.txtNewTag.setPreferredSize(DIM_TEXTFIELDS);
		this.txtNewTag.setMaximumSize(DIM_TEXTFIELDS);
		this.add(this.txtNewTag, constraints);

		gridy++;
		constraints.gridx = 1;
		constraints.gridy = gridy;
		btnAddTag.addActionListener(new ActionListener() {

			/**
			 * Invoked when the button is pressed.
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				addNewTag();
			}
		});
		this.add(btnAddTag, constraints);

		gridy++;
		constraints.gridx = 1;
		constraints.gridy = gridy;
		btnDelTag.addActionListener(new ActionListener() {

			/**
			 * Invoked when the button is pressed.
			 */
			@Override
			public void actionPerformed(final ActionEvent event) {
				removeSelectedTags();
			}
		});
		this.add(btnDelTag, constraints);
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
	private final void sortTags() {
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
	private void removeTag(final int index) {
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

	/**
	 * Remove the selected tags from the formula.
	 */
	public void removeSelectedTags() {
		while (!this.lstTags.isSelectionEmpty()) {
			final int selected = this.lstTags.getSelectedIndex();
			removeTag(selected);
		}
	}

	/**
	 * Add a new tag to the list.
	 * The name comes from the provided text field.
	 */
	public void addNewTag() {
		final String strTag = this.txtNewTag.getText();
		if (!AFTER_ADD.equals(strTag) && !this.txtNewTag.getText().isEmpty()) {
			this.txtNewTag.setText(AFTER_ADD);
			final FormulaTag tag = new FormulaTag();
			tag.setName(strTag);
			this.tags.add(tag);
			sortTags();
		}
	}

}
