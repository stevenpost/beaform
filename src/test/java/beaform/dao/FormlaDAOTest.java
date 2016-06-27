package beaform.dao;

import java.util.concurrent.Callable;

import org.apache.commons.collections.ListUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import beaform.debug.DebugUtils;
import beaform.entities.Formula;
import beaform.search.SearchFormulaTask;
import junit.framework.TestCase;

/**
 * Test for the formula DA.
 *
 * @author Steven Post
 *
 */
public class FormlaDAOTest extends TestCase {

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Before
	public void setUp() {
		GraphDbHandler.initInstance("test");
		DebugUtils.clearDb();
	}

	/**
	 * Test for adding a new formula.
	 * @throws Exception
	 */
	@Test
	public void testAddFormula() throws Exception {
		final Formula testForm = new Formula("Testform", "Description", "100g");
		FormulaDAO.addFormula("Testform", "Description", "100g", ListUtils.EMPTY_LIST, ListUtils.EMPTY_LIST);
		final Callable<Formula> task = new SearchFormulaTask("Testform");
		final Formula result = task.call();
		assertEquals("This isn't the expected formula", testForm,  result);
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
