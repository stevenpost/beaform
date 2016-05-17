package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;

import beaform.entities.FormulaTag;

/**
 * An action that deletes a tag from the tag list.
 *
 * @author Steven Post
 *
 */
public final class DelTagAction implements ActionListener {

	/** The graphical list of tags */
	private final transient JList<FormulaTag> lstTags;

	/** The tag panel */
	private final transient TagPane tagPane;

	/**
	 * Constructor.
	 * @param lstTags the graphical list of tags
	 * @param tagPane the {@link TagPane}
	 */
	public DelTagAction(final JList<FormulaTag> lstTags, final TagPane tagPane) {
		this.lstTags = lstTags;
		this.tagPane = tagPane;
	}

	/**
	 * Invoked when the action occurs.
	 *
	 * @param event The event object.
	 */
	@Override
	public void actionPerformed(final ActionEvent event) {
		while (!this.lstTags.isSelectionEmpty()) {
			final int selected = this.lstTags.getSelectedIndex();
			this.tagPane.removeTag(selected);
		}
	}
}