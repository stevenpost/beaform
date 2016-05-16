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
		final TransactionManager tm = GraphDbHandlerForJTA.getInstance().getTransactionManager();

		try {
			tm.begin();
		}
		catch (NotSupportedException | SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

		final EntityManager ementityManager = GraphDbHandlerForJTA.getInstance().createNewEntityManager();

		final String query = "MATCH n OPTIONAL MATCH (n)-[r]-() DELETE n,r";
		try {
			ementityManager.createNativeQuery(query).getSingleResult();
		}
		catch (NoResultException nre) {
			// We delete everything, there won't be a result.
		}

		ementityManager.flush();
		ementityManager.close();

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