package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import beaform.entities.FormulaTag;

/**
 * Action that adds a new tag to {@link TagPane}.
 *
 * @author Steven Post
 *
 */
public final class AddTagAction implements ActionListener {

	/** What text should be in the text field after adding the tag */
	private static final String AFTER_ADD = "";

	/** The text field with the name of the new tag */
	private final transient JTextField txtNewTag;

	/** The {@link TagPane} that displays the tags */
	private final transient TagPane tagPane;

	/**
	 * Constructor.
	 *
	 * @param txtNewTag the text field with the name of the new tag
	 * @param tagPane The {@link TagPane} that displays the tags
	 */
	public AddTagAction(final JTextField txtNewTag, final TagPane tagPane) {
		this.txtNewTag = txtNewTag;
		this.tagPane = tagPane;
	}

	/**
	 * This method is called when the event occurs.
	 *
	 * @param event The event object.
	 */
	@Override
	public void actionPerformed(final ActionEvent event) {
		final String strTag = this.txtNewTag.getText();
		if (!AFTER_ADD.equals(strTag) && !this.txtNewTag.getText().isEmpty()) {
			this.txtNewTag.setText(AFTER_ADD);
			final FormulaTag tag = new FormulaTag();
			tag.setName(strTag);
			this.tagPane.addTag(tag);
		}
	}
}