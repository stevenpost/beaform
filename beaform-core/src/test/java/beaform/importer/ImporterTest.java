package beaform.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.FormulaDAO;
import beaform.dao.GraphDbHandler;
import beaform.debug.DebugUtils;
import beaform.entities.Formula;

@SuppressWarnings("static-method")
public class ImporterTest {

	private static final Logger LOG = LoggerFactory.getLogger(ImporterTest.class);
	private static final String ALL_FORMULAS = "match (n:Formula) return count(n)";

	private final String importFilePath = "src/test/resources/importer/input.xml";

	@Before
	public void setUp() {
		GraphDbHandler.initInstance("test");
		DebugUtils.clearDb();
	}

	@Test
	public void testImporter() throws ImporterException {
		Importer importer = new Importer();

		File importFile = new File(this.importFilePath);
		importer.importFromFile(importFile);

		assertEquals("The DB hasn't got the expected number of formulas", 4L, countFormulasInDb());
		// Get more information on the DB content
		Formula form1 = FormulaDAO.findFormulaByName("testForm1");
		final List<String> tagsForm1 = Arrays.asList(new String[]{"first","second"});
		validateFormula(form1, "Some long description", "200g", tagsForm1);

		Formula form3 = FormulaDAO.findFormulaByName("testForm3");
		assertEquals("This isn't the expected description", "third test formula", form3.getDescription());
		final List<String> tagsForm3 = Arrays.asList(new String[]{"second"});
		validateFormula(form3, "third test formula", "10g", tagsForm3);
	}

	@After
	public void tearDown() {
		DebugUtils.clearDb();
		GraphDbHandler.clearInstance();
	}

	private static long countFormulasInDb() {
		final EntityManager entityManager = GraphDbHandler.getInstance().getEntityManager();
		entityManager.getTransaction().begin();

		final Query query = entityManager.createNativeQuery(ALL_FORMULAS);
		final long formulaCount = ((Long) query.getSingleResult()).longValue();


		entityManager.getTransaction().commit();

		LOG.debug(ALL_FORMULAS);
		return formulaCount;
	}

	private void validateFormula(final Formula form, final String description, final String totalAmount, final List<String> tags) {
		assertEquals("This isn't the expected description", description, form.getDescription());
		assertEquals("This isn't the expected total amount for this formula", totalAmount, form.getTotalAmount());
		List<String> tagsFromForm = form.getTagsAsStrings();

		for (String tag : tags) {
			assertTrue("The tag " + tag + "couldn't be found", tagsFromForm.contains(tag));
		}
		assertEquals("This isn't the expected number of tags", tags.size(), tagsFromForm.size());
	}
}
