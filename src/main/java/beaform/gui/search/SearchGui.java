package beaform.gui.search;

import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A search GUI.
 *
 * @author Steven Post
 *
 */
public class SearchGui extends JPanel {

	/** A serial. */
	private static final long serialVersionUID = 2557014310487638917L;

	/** The dimensions for most text fields */
	private static final Dimension DIM_TXTFIELDS = new Dimension(100, 30);

	/** The field to type in the search */
	private final JTextField txtSearchTag = new JTextField();

	/** A button to kickoff the search */
	private final JButton btnSearch = new JButton("Search");

	/** A combo box to define the type of search */
	private final JComboBox<String> cmbType = new JComboBox<String>();

	/**
	 * Constructor.
	 */
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
		this.cmbType.addItem("Tag");
		this.cmbType.setSelectedIndex(0);
		this.add(this.cmbType, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		this.add(this.btnSearch, constraints);
		this.btnSearch.addActionListener(new SearchAction(this.txtSearchTag, this, this.cmbType));


	}

}
