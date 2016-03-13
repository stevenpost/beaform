package beaform.gui.search;

import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchGui extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 2557014310487638917L;

	private static final Dimension txtFieldDimensions = new Dimension(100, 30);
	private final JTextField txtSearchTag = new JTextField();
	private final JButton btnSearch = new JButton("Search");
	private final JComboBox<String> cmbType = new JComboBox<String>();

	public SearchGui() {
		super();

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.txtSearchTag.setMinimumSize(txtFieldDimensions);
		this.txtSearchTag.setPreferredSize(txtFieldDimensions);
		this.txtSearchTag.setMaximumSize(txtFieldDimensions);
		this.add(this.txtSearchTag, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
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
		this.btnSearch.addActionListener(new SearchAction(this.txtSearchTag, this.cmbType));
		this.add(this.btnSearch, constraints);


	}

}
