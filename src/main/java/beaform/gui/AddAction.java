package beaform.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.persistence.EntityManager;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.GraphDbHandlerForJTA;
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

		TransactionManager tm = GraphDbHandlerForJTA.getInstance().getTransactionManager();

		try {
			tm.begin();
		}
		catch (NotSupportedException | SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

		final EntityManager em = GraphDbHandlerForJTA.getInstance().getNewEntityManager();

		if (this.cmbType.getSelectedItem() == "Base") {
			Base newBase = new Base();
			newBase.setName(this.txtNameField.getText());
			newBase.setDescription(this.txtDescriptionField.getText());
			em.persist(newBase);
		}
		else if (this.cmbType.getSelectedItem() == "Formula") {
			Formula newForm = new Formula();
			newForm.setName(this.txtNameField.getText());
			newForm.setDescription(this.txtDescriptionField.getText());
			em.persist(newForm);
		}

		em.flush();
		em.close();

		try {
			tm.commit();
		}
		catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
