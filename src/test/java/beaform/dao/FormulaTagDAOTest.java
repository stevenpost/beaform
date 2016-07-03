package beaform.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.debug.DebugUtils;
import beaform.entities.FormulaTag;
import junit.framework.TestCase;

/**
 * Test for the tag DAO.
 *
 * @author Steven Post
 *
 */
public class FormulaTagDAOTest extends TestCase {

	private static final Logger LOG = LoggerFactory.getLogger(FormulaTagDAOTest.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Before
	public void setUp() {
		GraphDbHandler.initInstance("test");
		DebugUtils.clearDb();
	}

	@Test
	public void testFindTagByName() {
		DebugUtils.fillDb();
		final EntityManager entitymanager = GraphDbHandler.getInstance().getEntityManager();
		entitymanager.getTransaction().begin();

		final FormulaTag tag = FormulaTagDAO.findByObject(new FormulaTag("First"));

		entitymanager.getTransaction().commit();

		assertNotNull("The tag wasn't found", tag);
		assertEquals("This isn't the expected formula", "First", tag.getName());
	}

	@Test(expected=NoResultException.class)
	public void testFindTagByNameNoResult() {
		DebugUtils.fillDb();
		final EntityManager entitymanager = GraphDbHandler.getInstance().getEntityManager();
		try {
		entitymanager.getTransaction().begin();

		FormulaTagDAO.findByObject(new FormulaTag("Form1"));

		entitymanager.getTransaction().commit();
		}
		catch (PersistenceException pe) {
			if (entitymanager.getTransaction().isActive()) {
				entitymanager.getTransaction().rollback();
			}
			LOG.debug("Something went wrong", pe);
		}
	}

	@Override
	@After
	public void tearDown() {
		DebugUtils.clearDb();
		GraphDbHandler.clearInstance();
	}

}
