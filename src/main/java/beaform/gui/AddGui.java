package beaform.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import beaform.entities.Tag;

public class AddGui extends JPanel {

	public static final class DelTagAction implements ActionListener {

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

	public static final class AddTagAction implements ActionListener {

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

	/**
	 *
	 */
	private static final long serialVersionUID = 2557014310487638917L;

	private static final Dimension txtFieldDimensions = new Dimension(100, 30);
	private static final Dimension lstFieldDimensions = new Dimension(100, 90);
	private static final JLabel lblName = new JLabel("Name");
	private static final JLabel lblDescription = new JLabel("Description");

	private final JTextField txtName = new JTextField();
	private final JTextField txtDescription = new JTextField();
	private final DefaultListModel<Tag> lstModel = new DefaultListModel<Tag>();
	private final JList<Tag> lstTags = new JList<Tag>(this.lstModel);
	private final JTextField txtNewTag = new JTextField();
	private final JButton btnAddTag = new JButton("Add Tag");
	private final JButton btnDelTag = new JButton("Remove Tag");
	private final JButton btnSubmit = new JButton("Submit");

	public AddGui() {
		super(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.add(lblName, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		this.txtName.setMinimumSize(txtFieldDimensions);
		this.txtName.setPreferredSize(txtFieldDimensions);
		this.txtName.setMaximumSize(txtFieldDimensions);
		this.add(this.txtName, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		this.add(lblDescription, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		this.txtDescription.setMinimumSize(txtFieldDimensions);
		this.txtDescription.setPreferredSize(txtFieldDimensions);
		this.txtDescription.setMaximumSize(txtFieldDimensions);
		this.add(this.txtDescription, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridheight = 3;
		this.lstTags.setMinimumSize(lstFieldDimensions);
		this.lstTags.setPreferredSize(lstFieldDimensions);
		this.lstTags.setMaximumSize(lstFieldDimensions);
		this.add(this.lstTags, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridheight = 1;
		this.txtNewTag.setMinimumSize(txtFieldDimensions);
		this.txtNewTag.setPreferredSize(txtFieldDimensions);
		this.txtNewTag.setMaximumSize(txtFieldDimensions);
		this.add(this.txtNewTag, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		this.btnAddTag.addActionListener(new AddTagAction(this.txtNewTag, this.lstModel));
		this.add(this.btnAddTag, constraints);

		constraints.gridx = 1;
		constraints.gridy = 4;
		this.btnDelTag.addActionListener(new DelTagAction(this.lstTags, this.lstModel));
		this.add(this.btnDelTag, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 2;
		this.btnSubmit.addActionListener(new AddAction(this.txtName, this.txtDescription, this.lstModel));
		this.add(this.btnSubmit, constraints);

	}

}
