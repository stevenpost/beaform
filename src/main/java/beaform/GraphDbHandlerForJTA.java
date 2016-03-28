package beaform;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import beaform.entities.Formula;

public class GraphDbHandlerForJTA {

	public static void main(String[] args) throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ogm-jpa-tutorial");
		TransactionManager transactionManager =
						com.arjuna.ats.jta.TransactionManager.transactionManager();

		System.out.println("Got further");

		//note that you must start the transaction before creating the EntityManager
		//or else call entityManager.joinTransaction()
		transactionManager.begin();

		final EntityManager em = emf.createEntityManager();

		Formula testForm = new Formula();
		testForm.setName("L'albatros");
		em.persist(testForm);

		transactionManager.commit();

		em.clear();

		em.close();

		emf.close();

		System.out.println("Got to the end");

	}
}
