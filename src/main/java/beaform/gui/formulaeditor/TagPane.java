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
public final class TagPane extends JPanel {

	private static final long serialVersionUID = -6447838745127288741L;

	/** What text should be in the text field after adding the tag */
	private static final String AFTER_ADD = "";
	private static final int NAME_WIDTH = 100;
	private static final int NAME_HEIGHT = 30;
	private static final int LIST_WIDTH = 200;
	private static final int LIST_HEIGHT = 90;
	private static final double DEFAULT_X_WEIGTH = 0.1;
	private static final double DEFAULT_Y_WEIGTH = 0.1;
	private static final int TAGLABEL_COLS = 2;
	private static final int TAGLIST_ROWS = 3;

	private final List<FormulaTag> tags = new ArrayList<>();

	/** A list model to get the list of tags to the screen */
	private final DefaultListModel<FormulaTag> lstTagModel = new DefaultListModel<>();

	private final JList<FormulaTag> lstTags = new JList<>(this.lstTagModel);
	private final JTextField txtNewTag = new JTextField();


	public TagPane() {
		super(new GridBagLayout());
		init();
	}

	private void init() {
		final Dimension nameSize = new Dimension(NAME_WIDTH, NAME_HEIGHT);
		final Dimension listSize = new Dimension(LIST_WIDTH, LIST_HEIGHT);
		final JLabel tagLabel = new JLabel("Tags", SwingConstants.CENTER);

		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = DEFAULT_X_WEIGTH;
		constraints.weighty = DEFAULT_Y_WEIGTH;

		int gridy = 0;
		final JButton btnAddTag = new JButton("Add Tag");
		final JButton btnDelTag = new JButton("Remove Tag");

		constraints.gridx = 0;
		constraints.gridy = gridy;
		constraints.gridwidth = TAGLABEL_COLS;
		Utilities.setBoldFont(tagLabel);
		this.add(tagLabel, constraints);
		constraints.gridwidth = 1;

		gridy++;
		constraints.gridx = 0;
		constraints.gridy = gridy;
		constraints.gridheight = TAGLIST_ROWS;
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

	public void addMultipleTags(final Iterator<FormulaTag> tagList) {
		while (tagList.hasNext()) {
			this.tags.add(tagList.next());
		}
		sortTags();
	}

	/**
	 * This method sorts the tags alphabetically in the list.
	 * should the list model and the backing list be out of sync,
	 * this method will synchronize them.
	 */
	private void sortTags() {
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

	public Iterator<FormulaTag> getTags() {
		return this.tags.iterator();
	}

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
			final FormulaTag tag = new FormulaTag(strTag);
			this.tags.add(tag);
			sortTags();
		}
	}

}
