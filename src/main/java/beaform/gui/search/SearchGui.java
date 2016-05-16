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

	private static final Dimension DIM_TXTFIELDS = new Dimension(100, 30);
	private final JTextField txtSearchTag = new JTextField();
	private final JButton btnSearch = new JButton("Search");
	private final JComboBox<String> cmbType = new JComboBox<String>();

	public SearchGui() {
		super();

		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.txtSearchTag.setMinimumSize(DIM_TXTFIELDS);
		this.txtSearchTag.setPreferredSize(DIM_TXTFIELDS);
		this.txtSearchTag.setMaximumSize(DIM_TXTFIELDS);
		this.add(this.txtSearchTag, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		this.cmbType.setMinimumSize(DIM_TXTFIELDS);
		this.cmbType.setMaximumSize(DIM_TXTFIELDS);
		this.cmbType.setPreferredSize(DIM_TXTFIELDS);
		this.cmbType.setEditable(false);
		this.cmbType.addItem("Formula");
		this.cmbType.setSelectedIndex(0);
		this.add(this.cmbType, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		this.add(this.btnSearch, constraints);
		this.btnSearch.addActionListener(new SearchAction(this.txtSearchTag, this));


	}

}
