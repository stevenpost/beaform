package beaform.gui.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import beaform.GraphDbHandlerForJTA;
import beaform.entities.Formula;

public class FillDbEvent implements ActionListener {

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

		Formula form1 = new Formula();
		form1.setName("Form1");
		form1.setDescription("First test formula");

		em.persist(form1);

		Formula form2 = new Formula();
		form2.setName("Form2");
		form2.setDescription("Second test formula");

		em.persist(form2);

		Formula form3 = new Formula();
		form3.setName("Form3");
		form3.setDescription("Third test formula");

		em.persist(form3);

		Formula form4 = new Formula();
		form4.setName("Form4");
		form4.setDescription("Fourth test formula");

		em.persist(form4);

		// Add relationships
		form1.addIngredient(form3, "50%");
		form2.addIngredient(form4, "10%");
		form2.addIngredient(form1, "50%");

		em.persist(form1);
		em.persist(form4);

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

		System.out.println("stored");

	}

}
