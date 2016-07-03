package beaform.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import beaform.debug.DebugUtils;
import beaform.entities.FormulaTag;

/**
 * Test for the tag DAO.
 *
 * @author Steven Post
 *
 */
public class FormulaTagDAOTest {

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
			throw pe;
		}
	}

	@After
	public void tearDown() {
		DebugUtils.clearDb();
		GraphDbHandler.clearInstance();
	}

}
