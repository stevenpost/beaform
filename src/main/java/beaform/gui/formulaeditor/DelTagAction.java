package beaform.gui.formulaeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import beaform.entities.Tag;

public final class DelTagAction implements ActionListener {

	private final JList<Tag> lstTags;
	private final DefaultListModel<Tag> lstModel;

	public DelTagAction(JList<Tag> lstTags, DefaultListModel<Tag> lstModel) {
		this.lstTags = lstTags;
		this.lstModel = lstModel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		while (!this.lstTags.isSelectionEmpty()) {
			int selected = this.lstTags.getSelectedIndex();
			this.lstModel.remove(selected);
		}
	}
}