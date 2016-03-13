package beaform.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddGui extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 2557014310487638917L;

	private static final Dimension txtFieldDimensions = new Dimension(100, 30);
	private final JTextField txtName = new JTextField();
	private final JTextField txtDescription = new JTextField();
	private final JButton btnAdd = new JButton("Add");
	private final JComboBox<String> cmbType = new JComboBox<String>();

	public AddGui() {
		super();

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.txtName.setMinimumSize(txtFieldDimensions);
		this.txtName.setPreferredSize(txtFieldDimensions);
		this.txtName.setMaximumSize(txtFieldDimensions);
		this.add(this.txtName, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		this.txtDescription.setMinimumSize(txtFieldDimensions);
		this.txtDescription.setPreferredSize(txtFieldDimensions);
		this.txtDescription.setMaximumSize(txtFieldDimensions);
		this.add(this.txtDescription, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		this.cmbType.setMinimumSize(txtFieldDimensions);
		this.cmbType.setMaximumSize(txtFieldDimensions);
		this.cmbType.setPreferredSize(txtFieldDimensions);
		this.cmbType.setEditable(false);
		this.cmbType.addItem("Formula");
		this.cmbType.addItem("Base");
		this.cmbType.setSelectedIndex(0);
		this.add(this.cmbType, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		this.btnAdd.addActionListener(new AddAction(this.txtName, this.txtDescription, this.cmbType));
		this.add(this.btnAdd, constraints);

	}

}
