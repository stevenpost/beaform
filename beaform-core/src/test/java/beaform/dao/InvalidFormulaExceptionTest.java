package beaform.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import beaform.entities.InvalidFormulaException;

@SuppressWarnings("static-method")
public class InvalidFormulaExceptionTest {

	@Test
	public void testEmptyConstructor() {
		Exception e = new InvalidFormulaException();
		assertNull("There is a cause when there shoundln't", e.getCause());
		assertNull("There is a message when there shoundln't", e.getMessage());
	}

	@Test
	public void testMessageConstructor() {
		Exception e = new InvalidFormulaException("test message");
		assertEquals("The message is not the expected one", "test message", e.getMessage());
	}

}
