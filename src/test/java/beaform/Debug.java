package beaform;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.GraphDbHandlerForJTA;
import beaform.debug.DebugUtils;
import beaform.entities.Formula;
import junit.framework.TestCase;

/**
 * Test some debugging utilities
 *
 * @author Steven Post
 *
 */
public class Debug extends TestCase {

	/** a logger */
	private static final Logger LOG = LoggerFactory.getLogger(DebugUtils.class);

	/** The query to list all formulas */
	private static final String ALL_FORMULAS = "match (n:Formula) return n";

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Before
	public void setUp() {
		GraphDbHandlerForJTA.initInstance("test");
		DebugUtils.clearDb();
	}

	/**
	 * Test a simple cleanup.
	 */
	@Test
	public void testCleanupSimple() {
		DebugUtils.fillDb();
		DebugUtils.clearDb();
		assertEquals("Collection is not empty", 0, countFormulasInDb());
	}

	/**
	 * Test for filling the DB.
	 */
	@Test
	public void testFill() {
		DebugUtils.fillDb();
		assertEquals("Collection doesn't contain the expected amount of formulas", 4, countFormulasInDb());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@After
	public void tearDown() {
		DebugUtils.clearDb();
	}

	private int countFormulasInDb() {
		final TransactionManager transactionMgr = GraphDbHandlerForJTA.getTransactionManager();

		try {
			transactionMgr.begin();
		}
		catch (NotSupportedException | SystemException e1) {
			LOG.error(e1.getMessage(), e1);
			throw new IllegalStateException(e1);
		}

		final EntityManager entityManager = GraphDbHandlerForJTA.getNewEntityManager();

		final Query query = entityManager.createNativeQuery(ALL_FORMULAS, Formula.class);
		final List<Formula> formulas = query.getResultList();


		GraphDbHandlerForJTA.tryCloseEntityManager(entityManager);

		try {
			transactionMgr.commit();
		}
		catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
						| HeuristicRollbackException | SystemException e1) {
			LOG.error(e1.getMessage(), e1);
		}

		LOG.debug(ALL_FORMULAS);
		return formulas.size();
	}

}
