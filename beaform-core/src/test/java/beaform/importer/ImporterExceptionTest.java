package beaform.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

@SuppressWarnings("static-method")
public class ImporterExceptionTest {

	@Test
	public void testMessageConstructor() {
		Exception e = new ImporterException("test message");
		assertEquals("The message is not the expected one", "test message", e.getMessage());
	}

	@Test
	public void testCauseConstructor() {
		Exception e = new ImporterException(new Exception());
		assertNotNull("There is no cause when there shoundl be", e.getCause());
	}

	@Test
	public void testMessageAndCauseConstructor() {
		Exception e = new ImporterException("test message", new Exception());
		assertEquals("The message is not the expected one", "test message", e.getMessage());
		assertNotNull("There is no cause when there shoundl be", e.getCause());
	}

}
