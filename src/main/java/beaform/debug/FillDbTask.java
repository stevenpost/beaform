package beaform.debug;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.hibernate.ogm.exception.EntityAlreadyExistsException;

import beaform.GraphDbHandlerForJTA;
import beaform.entities.Formula;

public final class FillDbTask implements Runnable {
	@Override
	public void run() {
		TransactionManager tm = GraphDbHandlerForJTA.getInstance().getTransactionManager();

		try {
			tm.begin();
		}
		catch (NotSupportedException | SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

		try {
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
				System.out.println("stored");
			}
			catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
							| HeuristicRollbackException | SystemException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch(PersistenceException pe) {
			if (pe.getCause() instanceof EntityAlreadyExistsException) {
				System.out.println(pe.getMessage());
			}
			else {
				pe.printStackTrace();
			}
			try {
				tm.rollback();
				System.out.println("Transaction rolled back");
			}
			catch (IllegalStateException | SecurityException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}