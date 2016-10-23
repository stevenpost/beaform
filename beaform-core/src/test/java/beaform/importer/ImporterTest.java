package beaform.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.FormulaDAO;
import beaform.dao.GraphDbHandler;
import beaform.debug.DebugUtils;
import beaform.entities.Formula;
import beaform.entities.Ingredient;

@SuppressWarnings("static-method")
public class ImporterTest {

	private static final Logger LOG = LoggerFactory.getLogger(ImporterTest.class);
	private static final String ALL_FORMULAS = "match (n:Formula) return count(n) as i";

	private final String importFilePath = "src/test/resources/importer/input.xml";

	@Before
	public void setUp() {
		GraphDbHandler.initInstanceWithDbPath("neo4j_test/db");
		DebugUtils.clearDb();
	}

	@Test
	public void testImporter() throws ImporterException {
		File importFile = new File(this.importFilePath);
		Importer.importFromFile(importFile);

		assertEquals("The DB hasn't got the expected number of formulas", 4L, countFormulasInDb());
		// Get more information on the DB content
		Formula form1 = FormulaDAO.findFormulaByName("testForm1");
		final List<String> tagsForm1 = Arrays.asList(new String[]{"first","second"});
		final Map<String, String> ingredientsForm1 = new HashMap<>();
		ingredientsForm1.put("testForm2", "50g");
		ingredientsForm1.put("testForm3", "150g");
		validateFormula(form1, "Some long description", "200g", tagsForm1, ingredientsForm1);

		Formula form3 = FormulaDAO.findFormulaByName("testForm3");
		assertEquals("This isn't the expected description", "third test formula", form3.getDescription());
		final List<String> tagsForm3 = Arrays.asList(new String[]{"second"});
		validateFormula(form3, "third test formula", "10g", tagsForm3, Collections.emptyMap());
	}

	@After
	public void tearDown() {
		DebugUtils.clearDb();
	}

	private static long countFormulasInDb() {
		long formulaCount;
		final GraphDatabaseService graphDb = GraphDbHandler.getDbService();

		try ( Transaction tx = graphDb.beginTx(); ResourceIterator<Long> result = graphDb.execute(ALL_FORMULAS).columnAs("i"); ) {

			formulaCount = result.next().longValue();

			tx.success();
		}

		LOG.debug(ALL_FORMULAS);
		return formulaCount;
	}

	private void validateFormula(final Formula form, final String description, final String totalAmount, final List<String> tags, final Map<String, String> ingredients) {
		assertEquals("This isn't the expected description", description, form.getDescription());
		assertEquals("This isn't the expected total amount for this formula", totalAmount, form.getTotalAmount());
		List<String> tagsFromForm = form.getTagsAsStrings();

		for (String tag : tags) {
			assertTrue("The tag " + tag + " couldn't be found", tagsFromForm.contains(tag));
		}
		assertEquals("This isn't the expected number of tags", tags.size(), tagsFromForm.size());

		List<Ingredient> ingredientsFromForm = FormulaDAO.listIngredients(form);
		for (Ingredient ingredient : ingredientsFromForm) {
			final String ingredientName = ingredient.getFormula().getName();
			assertTrue("The ingredient " + ingredientName + " couldn't be found", ingredients.containsKey(ingredientName));
			assertEquals("The ingredient doesn't have the expected amount", ingredients.get(ingredientName), ingredient.getAmount());
		}
		assertEquals("This isn't the expected number of ingredients", ingredients.size(), ingredientsFromForm.size());
	}
}
