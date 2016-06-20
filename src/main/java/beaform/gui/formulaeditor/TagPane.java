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
import javax.swing.SwingConstants;

import beaform.entities.FormulaTag;
import beaform.gui.Utilities;

/**
 * Represents the GUI panel inside the editor to manage the tags.
 *
 * @author Steven Post
 *
 */
public class TagPane extends JPanel {

	/** What text should be in the text field after adding the tag */
	private static final String AFTER_ADD = "";

	/** A list of formula tags */
	private final List<FormulaTag> tags = new ArrayList<>();

	/** A list model to get the list of tags to the screen */
	private final DefaultListModel<FormulaTag> lstTagModel = new DefaultListModel<>();

	/** The graphical list of tags */
	private final JList<FormulaTag> lstTags = new JList<>(this.lstTagModel);

	/** A field for the name of the new tag */
	private final JTextField txtNewTag = new JTextField();

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -6447838745127288741L;

	/**
	 * Creates a tag panel.
	 */
	public TagPane() {
		super(new GridBagLayout());
		init();
	}

	private void init() {
		final Dimension nameSize = new Dimension(100, 30);
		final Dimension listSize = new Dimension(200, 90);
		final JLabel tagLabel = new JLabel("Tags", SwingConstants.CENTER);

		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.1;
		constraints.weighty = 0.1;

		int gridy = 0;
		final JButton btnAddTag = new JButton("Add Tag");
		final JButton btnDelTag = new JButton("Remove Tag");

		constraints.gridx = 0;
		constraints.gridy = gridy;
		constraints.gridwidth = 2;
		Utilities.setBoldFont(tagLabel);
		this.add(tagLabel, constraints);
		constraints.gridwidth = 1;

		gridy++;
		constraints.gridx = 0;
		constraints.gridy = gridy;
		constraints.gridheight = 3;
		constraints.fill = GridBagConstraints.BOTH;
		this.lstTags.setMinimumSize(listSize);
		this.lstTags.setPreferredSize(listSize);
		this.lstTags.setMaximumSize(listSize);
		this.add(this.lstTags, constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;

		constraints.gridx = 1;
		constraints.gridy = gridy;
		constraints.gridheight = 1;
		this.txtNewTag.setMinimumSize(nameSize);
		this.txtNewTag.setPreferredSize(nameSize);
		this.txtNewTag.setMaximumSize(nameSize);
		this.add(this.txtNewTag, constraints);

		gridy++;
		constraints.gridx = 1;
		constraints.gridy = gridy;
		btnAddTag.addActionListener(event -> addNewTag());
		this.add(btnAddTag, constraints);

		gridy++;
		constraints.gridx = 1;
		constraints.gridy = gridy;
		btnDelTag.addActionListener(event -> removeSelectedTags());
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

	private void removeTagFromList(final int index) {
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
			removeTagFromList(selected);
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
