package beaform.search;

import java.util.concurrent.Callable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import beaform.dao.GraphDbHandler;
import beaform.debug.DebugUtils;
import beaform.entities.Formula;
import junit.framework.TestCase;

/**
 * Tests for searching formulas by tag.
 *
 * @author Steven Post
 *
 */
@SuppressWarnings("static-method")
public class SearchFormulasTaskTest extends TestCase {

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Before
	public void setUp() {
		GraphDbHandler.initInstance("neo4j_test/db");
		DebugUtils.clearDb();
		DebugUtils.fillDb();
	}

	/**
	 * Test the search method.
	 * @throws Exception
	 */
	@Test
	public void testSearch() throws Exception {
		final Callable<Formula> task = new SearchFormulaTask("Form1");
		final Formula result = task.call();
		assertEquals("This isn't the tag we searched for", "Form1", result.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@After
	public void tearDown() {
		DebugUtils.clearDb();
	}

}
