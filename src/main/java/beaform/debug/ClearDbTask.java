package beaform.debug;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import beaform.GraphDbHandlerForJTA;

public class ClearDbTask implements Runnable {
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

		final EntityManager em = GraphDbHandlerForJTA.getInstance().getEntityManagerFactory().createEntityManager();

		String query = "MATCH n OPTIONAL MATCH (n)-[r]-() DELETE n,r";
		try {
			em.createNativeQuery(query).getSingleResult();
		}
		catch (NoResultException nre) {
			// We delete everything, there won't be a result.
		}

		em.flush();
		em.close();

		try {
			tm.commit();
			System.out.println("Cleared DB");
		}
		catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}