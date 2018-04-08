package beaform.commands;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Callable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaform.dao.FormulaDAO;
import beaform.dao.GraphDbHandler;
import beaform.debug.DebugUtils;
import beaform.entities.Formula;
import beaform.search.SearchFormulaTask;
import beaform.utilities.ErrorDisplay;

public class CommandsTest {

	private final ErrorDisplay errorDisplay = new LoggingErrorDisplay();

	@Before
	public void setUp() {
		GraphDbHandler.initInstanceWithDbPath("neo4j_test/db");
	}

	@Test
	public void testCreateCommand() {
		final Formula formulaToPersist = new Formula("testName", "testDesc", "100g");
		formulaToPersist.setNotes("Testing notes");

		Command cmd = new CreateNewFormulaCommand(formulaToPersist, this.errorDisplay);
		cmd.execute();

		final Formula resultingFormula = FormulaDAO.findFormulaByName("testName");
		assertEquals("The 2 formulas are not the same", formulaToPersist, resultingFormula);
	}

	@Test
	public void testUpdateCommand() throws Exception {
		DebugUtils.fillDb();
		final Formula formula = new Formula("Form1", "New description", "100g");

		Command cmd = new UpdateFormulaCommand(formula, this.errorDisplay);
		cmd.execute();

		final Callable<Formula> task = new SearchFormulaTask("Form1");
		final Formula result = task.call();
		assertEquals("This isn't the expected description", "New description", result.getDescription());
	}

	@After
	public void tearDown() {
		DebugUtils.clearDb();
	}

	private static class LoggingErrorDisplay implements ErrorDisplay {
		private static final Logger LOG = LoggerFactory.getLogger(LoggingErrorDisplay.class);

		public LoggingErrorDisplay() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void displayError(String error) {
			LOG.info(error);
		}

	}

}
