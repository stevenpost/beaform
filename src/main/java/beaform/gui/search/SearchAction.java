package beaform.gui.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.persistence.EntityManager;
import javax.swing.JTextField;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import beaform.GraphDbHandlerForJTA;
import beaform.entities.Formula;

public class SearchAction implements ActionListener {

	private final JTextField txtName;

	public SearchAction(JTextField name) {
		this.txtName = name;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
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

		String query = "match (n:Formula { name:'" + this.txtName.getText() + "' }) return n";
		Formula result = (Formula) em.createNativeQuery(query, Formula.class).getSingleResult();
		System.out.println("Found: " + result);

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
