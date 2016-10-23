package beaform.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Test for the GraphDbHandler.
 *
 * @author Steven Post
 *
 */
@SuppressWarnings("static-method")
public class GraphDbHandlerTest {

	@Test
	public void testGetInstance() {
		GraphDbHandler.initInstanceWithDbPath("neo4j_test/db");
		final GraphDbHandler handler = GraphDbHandler.getInstance();

		assertNotNull("The handler is null", handler);
	}

}
