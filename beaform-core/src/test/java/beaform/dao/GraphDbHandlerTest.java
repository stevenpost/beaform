package beaform.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Test for the GraphDbHandler.
 *
 * @author Steven Post
 *
 */
public class GraphDbHandlerTest {

	@Test(expected=IllegalStateException.class)
	public void testClearInstanceException() {
		GraphDbHandler.clearInstance();
	}

	@Test(expected=IllegalStateException.class)
	public void testGetInstanceException() {
		GraphDbHandler.getInstance();
	}

	@Test
	public void testGetInstance() {
		GraphDbHandler.initInstance("test");
		final GraphDbHandler handler = GraphDbHandler.getInstance();

		assertNotNull("The handler is null", handler);

		GraphDbHandler.clearInstance();
	}

}
