package beaform.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import beaform.debug.DebugUtils;
import beaform.entities.FormulaTag;
import beaform.entities.InvalidFormulaException;

/**
 * Test for the tag DAO.
 *
 * @author Steven Post
 *
 */
@SuppressWarnings("static-method")
public class FormulaTagDAOTest {

	@Before
	public void setUp() {
		GraphDbHandler.initInstanceWithDbPath("neo4j_test/db");
	}

	@Test
	public void testFindOrCreateWithoutData() {
		String name;

		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();
		try (Transaction tx = graphDb.beginTx()) {
			Node tag = FormulaTagDAO.findOrCreate(new FormulaTag("First"));
			name = (String) tag.getProperty("name");
			tx.success();
		}

		assertNotNull("The tag wasn't found", name);
		assertEquals("This isn't the expected formula", "First", name);
	}

	@Test
	public void testFindOrCreateWithData() {
		String name;

		DebugUtils.fillDb();
		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();
		try (Transaction tx = graphDb.beginTx()) {
			Node tag = FormulaTagDAO.findOrCreate(new FormulaTag("First"));
			name = (String) tag.getProperty("name");
			tx.success();
		}

		assertNotNull("The tag wasn't found", name);
		assertEquals("This isn't the expected formula", "First", name);
	}

	@Test
	public void testFindByName() {
		String name;

		DebugUtils.fillDb();
		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();
		try (Transaction tx = graphDb.beginTx()) {
			Node tag = FormulaTagDAO.findByName("First");
			name = (String) tag.getProperty("name");
			tx.success();
		}

		assertNotNull("The tag wasn't found", name);
		assertEquals("This isn't the expected formula", "First", name);
	}

	@Test(expected=InvalidFormulaException.class)
	public void testNodeToTagWithoutLabel() {
		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();
		try (Transaction tx = graphDb.beginTx()) {
			Node node = graphDb.createNode();
			FormulaTagDAO.nodeToTag(node);
			tx.success();
		}
	}

	@Test(expected=InvalidFormulaException.class)
	public void testNodeToTagWithInvalidLabel() {
		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();
		try (Transaction tx = graphDb.beginTx()) {
			Node node = graphDb.createNode(Label.label("dummylabel"));
			FormulaTagDAO.nodeToTag(node);
			tx.success();
		}
	}

	@Test
	public void testListAllTags() {
		List<FormulaTag> tags = FormulaTagDAO.listAllTags();
		assertEquals("This isn't the exted number of tags in the DB", 0, tags.size());
	}

	@Test
	public void testListAllTagsFilledDb() {
		DebugUtils.fillDb();
		List<FormulaTag> tags = FormulaTagDAO.listAllTags();
		assertEquals("This isn't the exted number of tags in the DB", 2, tags.size());
	}

	@After
	public void tearDown() {
		DebugUtils.clearDb();
	}

}
