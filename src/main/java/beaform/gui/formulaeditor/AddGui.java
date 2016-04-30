package beaform.gui.formulaeditor;

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

import beaform.entities.Formula;
import beaform.entities.Tag;

public class AddGui extends JPanel {

	public static final class DelIngredientAction implements ActionListener {

		private final DefaultListModel<Formula> lstFormulaModel;
		private final JList<Formula> lstFormulas;

		public DelIngredientAction(JList<Formula> lstFormulas, DefaultListModel<Formula> lstFormulaModel) {
			this.lstFormulaModel = lstFormulaModel;
			this.lstFormulas = lstFormulas;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			while (!this.lstFormulas.isSelectionEmpty()) {
				int selected = this.lstFormulas.getSelectedIndex();
				this.lstFormulaModel.remove(selected);
			}
		}
	}

	public static final class AddIngredient implements ActionListener {

		private final JTextField txtNewIngredient;
		private final DefaultListModel<Formula> lstFormulaModel;

		public AddIngredient(JTextField txtNewIngredient, DefaultListModel<Formula> lstFormulaModel) {
			this.txtNewIngredient = txtNewIngredient;
			this.lstFormulaModel = lstFormulaModel;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String strIngredient = this.txtNewIngredient.getText();
			if (!"".equals(strIngredient)) {
				this.txtNewIngredient.setText("");
				Formula form = new Formula();
				form.setName(strIngredient);
				this.lstFormulaModel.addElement(form);
			}
		}
	}

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
	private static final Dimension listDimensions = new Dimension(150, 90);
	private static final JLabel lblName = new JLabel("Name");
	private static final JLabel lblDescription = new JLabel("Description");
	private static final JLabel lblTags = new JLabel("Tags");
	private static final JLabel lblIngredients = new JLabel("Ingredients");

	private final JTextField txtName = new JTextField();
	private final JTextField txtDescription = new JTextField();

	private final DefaultListModel<Tag> lstTagModel = new DefaultListModel<Tag>();
	private final JList<Tag> lstTags = new JList<Tag>(this.lstTagModel);
	private final JTextField txtNewTag = new JTextField();
	private final JButton btnAddTag = new JButton("Add Tag");
	private final JButton btnDelTag = new JButton("Remove Tag");

	private final DefaultListModel<Formula> lstFormulaModel = new DefaultListModel<Formula>();
	private final JList<Formula> lstFormulas = new JList<Formula>(this.lstFormulaModel);
	private final JTextField txtNewIngredient = new JTextField();
	private final JButton btnAddIngredient = new JButton("Add ingedrient");
	private final JButton btnDelIngredient = new JButton("Del ingedrient");

	private final JButton btnSubmit = new JButton("Submit");

	public AddGui() {
		super(new GridBagLayout());
		int y = 0;

		// Formula requirements
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = y;
		this.add(lblName, constraints);

		constraints.gridx = 1;
		constraints.gridy = y;
		this.txtName.setMinimumSize(txtFieldDimensions);
		this.txtName.setPreferredSize(txtFieldDimensions);
		this.txtName.setMaximumSize(txtFieldDimensions);
		this.add(this.txtName, constraints);

		y++;
		constraints.gridx = 0;
		constraints.gridy = y;
		this.add(lblDescription, constraints);

		constraints.gridx = 1;
		constraints.gridy = y;
		this.txtDescription.setMinimumSize(txtFieldDimensions);
		this.txtDescription.setPreferredSize(txtFieldDimensions);
		this.txtDescription.setMaximumSize(txtFieldDimensions);
		this.add(this.txtDescription, constraints);

		// Ingredients
		y++;
		constraints.gridx = 0;
		constraints.gridy = y;
		constraints.gridwidth = 2;
		this.add(lblIngredients, constraints);
		constraints.gridwidth = 1;

		y++;
		constraints.gridx = 0;
		constraints.gridy = y;
		constraints.gridheight = 3;
		this.lstFormulas.setMinimumSize(listDimensions);
		this.lstFormulas.setPreferredSize(listDimensions);
		this.lstFormulas.setMaximumSize(listDimensions);
		this.add(this.lstFormulas, constraints);
		constraints.gridheight = 1;

		constraints.gridx = 1;
		constraints.gridy = y;
		this.txtNewIngredient.setMinimumSize(txtFieldDimensions);
		this.txtNewIngredient.setPreferredSize(txtFieldDimensions);
		this.txtNewIngredient.setMaximumSize(txtFieldDimensions);
		this.add(this.txtNewIngredient, constraints);

		y++;
		constraints.gridx = 1;
		constraints.gridy = y;
		this.btnAddIngredient.addActionListener(new AddIngredient(this.txtNewIngredient, this.lstFormulaModel));
		this.add(this.btnAddIngredient, constraints);

		y++;
		constraints.gridx = 1;
		constraints.gridy = y;
		this.btnDelIngredient.addActionListener(new DelIngredientAction(this.lstFormulas, this.lstFormulaModel));
		this.add(this.btnDelIngredient, constraints);

		// Tags
		y++;
		constraints.gridx = 0;
		constraints.gridy = y;
		constraints.gridwidth = 2;
		this.add(lblTags, constraints);
		constraints.gridwidth = 1;

		y++;
		constraints.gridx = 0;
		constraints.gridy = y;
		constraints.gridheight = 3;
		this.lstTags.setMinimumSize(listDimensions);
		this.lstTags.setPreferredSize(listDimensions);
		this.lstTags.setMaximumSize(listDimensions);
		this.add(this.lstTags, constraints);

		constraints.gridx = 1;
		constraints.gridy = y;
		constraints.gridheight = 1;
		this.txtNewTag.setMinimumSize(txtFieldDimensions);
		this.txtNewTag.setPreferredSize(txtFieldDimensions);
		this.txtNewTag.setMaximumSize(txtFieldDimensions);
		this.add(this.txtNewTag, constraints);

		y++;
		constraints.gridx = 1;
		constraints.gridy = y;
		this.btnAddTag.addActionListener(new AddTagAction(this.txtNewTag, this.lstTagModel));
		this.add(this.btnAddTag, constraints);

		y++;
		constraints.gridx = 1;
		constraints.gridy = y;
		this.btnDelTag.addActionListener(new DelTagAction(this.lstTags, this.lstTagModel));
		this.add(this.btnDelTag, constraints);

		// Submit
		y++;
		constraints.gridx = 0;
		constraints.gridy = y;
		constraints.gridwidth = 2;
		this.btnSubmit.addActionListener(new AddAction(this.txtName, this.txtDescription,this.lstFormulaModel, this.lstTagModel));
		this.add(this.btnSubmit, constraints);

	}

}
