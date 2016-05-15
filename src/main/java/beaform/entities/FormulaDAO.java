package beaform.entities;

import java.util.Iterator;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import beaform.GraphDbHandlerForJTA;

public class FormulaDAO {

	public Iterator<Entry<String, Formula>> getIngredients(Formula formula) throws NotSupportedException, SystemException {
		TransactionManager tm = GraphDbHandlerForJTA.getInstance().getTransactionManager();

		tm.begin();

		final EntityManager em = GraphDbHandlerForJTA.getInstance().getEntityManagerFactory().createEntityManager();
		formula = (Formula) em.createNativeQuery("match (n:Formula { name:'" + formula.getName() + "' }) return n", Formula.class).getSingleResult();
		Iterator<Entry<String, Formula>> retIt = formula.getIngredients();

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

		return retIt;
	}

}
