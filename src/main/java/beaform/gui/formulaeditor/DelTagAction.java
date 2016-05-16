package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;

import beaform.entities.Tag;

public final class DelTagAction implements ActionListener {

	private final JList<Tag> lstTags;
	private final FormulaEditor formulaEditor;

	public DelTagAction(JList<Tag> lstTags, FormulaEditor formulaEditor) {
		this.lstTags = lstTags;
		this.formulaEditor = formulaEditor;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		while (!this.lstTags.isSelectionEmpty()) {
			int selected = this.lstTags.getSelectedIndex();
			this.formulaEditor.removeTag(selected);
		}
	}
}