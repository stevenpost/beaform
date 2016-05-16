package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import beaform.entities.FormulaTag;

public final class AddTagAction implements ActionListener {

	private final JTextField txtNewTag;
	private final FormulaEditor formulaEditor;

	public AddTagAction(JTextField txtNewTag, FormulaEditor formulaEditor) {
		this.txtNewTag = txtNewTag;
		this.formulaEditor = formulaEditor;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String strTag = this.txtNewTag.getText();
		if (!"".equals(strTag)) {
			this.txtNewTag.setText("");
			FormulaTag tag = new FormulaTag();
			tag.setName(strTag);
			this.formulaEditor.addTag(tag);
		}
	}
}