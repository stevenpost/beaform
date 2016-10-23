package beaform;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.GraphDbHandler;
import beaform.debug.AsyncDebugUtils;
import beaform.debug.DebugUtils;

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
		GraphDbHandler.initInstanceWithDbPath("neo4j_test/db");
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
	}

	private static int countFormulasInDb() {
		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();
		Label label = Label.label("Formula");
		int formCount = 0;
		try ( Transaction tx = graphDb.beginTx() ) {
			try ( ResourceIterator<Node> formulas = graphDb.findNodes(label)) {
				while ( formulas.hasNext()) {
					formulas.next();
					formCount++;
				}
			}
			tx.success();
		}

		LOG.debug(ALL_FORMULAS);
		return formCount;
	}

}
