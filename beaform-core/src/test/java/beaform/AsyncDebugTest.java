package beaform;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.GraphDbHandler;
import beaform.debug.AsyncDebugUtils;
import beaform.debug.DebugUtils;
import beaform.entities.Formula;

/**
 * Test some debugging utilities
 *
 * @author Steven Post
 *
 */
@SuppressWarnings("static-method")
public class AsyncDebugTest {

	private static final Logger LOG = LoggerFactory.getLogger(AsyncDebugTest.class);

	private static final String ALL_FORMULAS = "match (n:Formula) return n";

	@Before
	public void setUp() {
		GraphDbHandler.initInstance("test");
		DebugUtils.clearDb();
	}

	@Test
	public void testCleanupSimple() throws InterruptedException, ExecutionException {
		AsyncDebugUtils.fillDb().get();
		AsyncDebugUtils.clearDb().get();
		assertEquals("Collection is not empty", 0, countFormulasInDb());
	}

	@Test
	public void testFill() throws InterruptedException, ExecutionException {
		AsyncDebugUtils.fillDb().get();
		AsyncDebugUtils.listAllFormulas().get();
		assertEquals("Collection doesn't contain the expected amount of formulas", 4, countFormulasInDb());
	}

	@After
	public void tearDown() {
		DebugUtils.clearDb();
		GraphDbHandler.clearInstance();
	}

	private static int countFormulasInDb() {
		final EntityManager entityManager = GraphDbHandler.getInstance().getEntityManager();
		entityManager.getTransaction().begin();

		final Query query = entityManager.createNativeQuery(ALL_FORMULAS, Formula.class);
		final List<Formula> formulas = query.getResultList();


		entityManager.getTransaction().commit();

		LOG.debug(ALL_FORMULAS);
		return formulas.size();
	}

}
