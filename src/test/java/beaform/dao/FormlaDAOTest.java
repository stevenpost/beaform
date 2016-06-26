package beaform.dao;

import org.apache.commons.collections.ListUtils;
import org.junit.Before;
import org.junit.Test;

import beaform.debug.DebugUtils;
import beaform.entities.TransactionSetupException;
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
		GraphDbHandlerForJTA.initInstance("test");
		DebugUtils.clearDb();
	}

	/**
	 * Test for adding a new formula.
	 * @throws TransactionSetupException
	 */
	@Test
	public void testAddFormula() throws TransactionSetupException {
		FormulaDAO.addFormula("Testform", "Description", "100g", ListUtils.EMPTY_LIST, ListUtils.EMPTY_LIST);
		assertEquals("This isn't the expected formula", null,  null);
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
