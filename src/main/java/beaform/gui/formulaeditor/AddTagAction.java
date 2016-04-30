package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JTextField;

import beaform.entities.Tag;

public final class AddTagAction implements ActionListener {

	private final JTextField txtNewTag;
	private final DefaultListModel<Tag> lstModel;

	public AddTagAction(JTextField txtNewTag, DefaultListModel<Tag> lstModel) {
		this.txtNewTag = txtNewTag;
		this.lstModel = lstModel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String strTag = this.txtNewTag.getText();
		if (!"".equals(strTag)) {
			this.txtNewTag.setText("");
			Tag tag = new Tag();
			tag.setName(strTag);
			this.lstModel.addElement(tag);
		}
	}
}