package beaform.gui.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.Search;
import beaform.entities.Formula;

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
			Iterator<Formula> formulas = search.search().get();
			while (formulas.hasNext()) {
				Formula formula = formulas.next();

				System.out.println(formula.toString());

			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
