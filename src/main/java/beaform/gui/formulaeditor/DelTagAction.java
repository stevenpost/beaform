package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;

import beaform.entities.FormulaTag;

public final class DelTagAction implements ActionListener {

	private final JList<FormulaTag> lstTags;
	private final FormulaEditor formulaEditor;

	public DelTagAction(final JList<FormulaTag> lstTags, final FormulaEditor formulaEditor) {
		this.lstTags = lstTags;
		this.formulaEditor = formulaEditor;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		while (!this.lstTags.isSelectionEmpty()) {
			final int selected = this.lstTags.getSelectedIndex();
			this.formulaEditor.removeTag(selected);
		}
	}
}