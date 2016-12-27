package beaform.dao;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import beaform.debug.DebugUtils;
import beaform.entities.BaseCompound;

@SuppressWarnings("static-method")
public class BaseCompoundDAOTest {

	@Before
	public void setUp() {
		GraphDbHandler.initInstanceWithDbPath("neo4j_test/db");
		DebugUtils.clearDb();
	}

	@Test
	public void testPersistBaseCompound() {
		final String testName = "dark water";
		final BaseCompound bc = new BaseCompound(testName);

		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();
		try (Transaction tx = graphDb.beginTx()) {
			BaseCompoundDAO.findOrCreate(bc);
			tx.success();
		}

		BaseCompound retrievedCompound;
		try (Transaction tx = graphDb.beginTx()) {
			retrievedCompound = BaseCompoundDAO.findByName(testName);
			tx.success();
		}

		assertEquals("This isn't the expected BaseCompound", bc, retrievedCompound);
	}

	@After
	public void tearDown() {
		DebugUtils.clearDb();
	}

}
