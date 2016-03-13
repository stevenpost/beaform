package beaform.gui.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.Search;

public class SearchAction implements ActionListener {

	private static final Logger LOG = LoggerFactory.getLogger(SearchAction.class);

	private final JTextField txtSearchTag;
	private final JComboBox<String> cmbType;

	public SearchAction(JTextField txtSearchTag, JComboBox<String> cmbType) {
		this.txtSearchTag = txtSearchTag;
		this.cmbType = cmbType;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LOG.info("Search: " + this.txtSearchTag.getText() + " of type " + this.cmbType.getSelectedItem());
		Search search = new Search(this.cmbType.getSelectedItem().toString());
		try {
			search.search().wait();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
