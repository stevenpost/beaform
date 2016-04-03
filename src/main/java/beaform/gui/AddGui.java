package beaform.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddGui extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 2557014310487638917L;

	private static final Dimension txtFieldDimensions = new Dimension(100, 30);
	private static final JLabel lblName = new JLabel("Name");
	private static final JLabel lblDescription = new JLabel("Description");

	private final JTextField txtName = new JTextField();
	private final JTextField txtDescription = new JTextField();
	private final JButton btnAdd = new JButton("Add");

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

		constraints.gridx = 2;
		constraints.gridy = 1;
		this.btnAdd.addActionListener(new AddAction(this.txtName, this.txtDescription));
		this.add(this.btnAdd, constraints);

	}

}
