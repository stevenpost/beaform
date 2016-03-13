package beaform.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.neo4j.graphdb.GraphDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.GraphDbHandler;
import beaform.entities.Base;
import beaform.entities.Formula;

public class AddAction implements ActionListener {

	private static final Logger LOG = LoggerFactory.getLogger(AddAction.class);

	private final JTextField txtNameField;
	private final JTextField txtDescriptionField;
	private final JComboBox<String> cmbType;

	public AddAction(JTextField txtNameField, JTextField txtDescriptionField, JComboBox<String> cmbType) {
		this.txtNameField = txtNameField;
		this.txtDescriptionField = txtDescriptionField;
		this.cmbType = cmbType;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LOG.info("Add: " + this.txtNameField.getText() + " of type " + this.cmbType.getSelectedItem() + " with description: " + this.txtDescriptionField.getText());
		GraphDatabaseService graphDb = GraphDbHandler.getInstance().getDbHandle();

		if (this.cmbType.getSelectedItem() == "Base") {
			Base newBase = new Base(this.txtNameField.getText(), this.txtDescriptionField.getText());
			newBase.persist(graphDb);
		}
		else if (this.cmbType.getSelectedItem() == "Formula") {
			Formula newForm = new Formula(this.txtNameField.getText(), this.txtDescriptionField.getText());
			newForm.persist(graphDb);
		}
	}

}
