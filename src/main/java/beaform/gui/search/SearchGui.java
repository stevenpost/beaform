package beaform.gui.search;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchGui extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 2557014310487638917L;

	private final JTextField txtSearchTag = new JTextField();

	public SearchGui() {
		super();

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.add(this.txtSearchTag, constraints);
	}

}
