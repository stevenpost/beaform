package beaform.search;

import java.util.List;
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
public class SearchFormulasByTagTaskTest extends TestCase {

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Before
	public void setUp() {
		GraphDbHandler.initInstance("test");
		DebugUtils.clearDb();
		DebugUtils.fillDb();
	}

	/**
	 * Test the search method.
	 * @throws Exception
	 */
	@Test
	public void testSearch() throws Exception {
		final Callable<List<Formula>> task = new SearchFormulasByTagTask("First");
		final List<Formula> resultList = task.call();
		for (final Formula form : resultList) {
			assertTrue("This formula doesn't match the search criteria", form.getTagsAsStrings().contains("First"));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@After
	public void tearDown() {
		DebugUtils.clearDb();
		GraphDbHandler.clearInstance();
	}

}
