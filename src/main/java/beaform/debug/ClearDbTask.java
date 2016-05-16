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

	/**
	 * Query to clear everything
	 */
	private static final String DELETE_QUERY = "MATCH n OPTIONAL MATCH (n)-[r]-() DELETE n,r";

	@Override
	public void run() {
		final TransactionManager transactionMgr = GraphDbHandlerForJTA.getInstance().getTransactionManager();

		try {
			transactionMgr.begin();
		}
		catch (NotSupportedException | SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

		final EntityManager ementityManager = GraphDbHandlerForJTA.getInstance().createNewEntityManager();

		try {
			ementityManager.createNativeQuery(DELETE_QUERY).getSingleResult();
		}
		catch (NoResultException nre) {
			// We delete everything, there won't be a result.
		}

		ementityManager.flush();
		ementityManager.close();

		try {
			transactionMgr.commit();
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