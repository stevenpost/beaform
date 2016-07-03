package beaform;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.GraphDbHandler;
import beaform.debug.DebugUtils;
import beaform.entities.Formula;

/**
 * Test some debugging utilities
 *
 * @author Steven Post
 *
 */
public class DebugTest {

	private static final Logger LOG = LoggerFactory.getLogger(DebugUtils.class);

	private static final String ALL_FORMULAS = "match (n:Formula) return n";

	@Before
	public void setUp() {
		GraphDbHandler.initInstance("test");
		DebugUtils.clearDb();
	}

	@Test
	public void testCleanupSimple() {
		DebugUtils.fillDb();
		DebugUtils.clearDb();
		assertEquals("Collection is not empty", 0, countFormulasInDb());
	}

	@Test
	public void testFill() {
		DebugUtils.fillDb();
		DebugUtils.listAllFormulas();
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
