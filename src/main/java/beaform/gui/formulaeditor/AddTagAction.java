package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import beaform.entities.FormulaTag;

public final class AddTagAction implements ActionListener {

	private final JTextField txtNewTag;
	private final FormulaEditor formulaEditor;

	public AddTagAction(final JTextField txtNewTag, final FormulaEditor formulaEditor) {
		this.txtNewTag = txtNewTag;
		this.formulaEditor = formulaEditor;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		final String strTag = this.txtNewTag.getText();
		if (!"".equals(strTag)) {
			this.txtNewTag.setText("");
			final FormulaTag tag = new FormulaTag();
			tag.setName(strTag);
			this.formulaEditor.addTag(tag);
		}
	}
}