package beaform.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

@SuppressWarnings("static-method")
public class InvalidFormulaExceptionTest {

	@Test
	public void testEmptyConstructor() {
		Exception e = new InvalidFormulaException();
		assertNull(e.getCause());
		assertNull(e.getMessage());
	}

	@Test
	public void testMessageConstructor() {
		Exception e = new NoSuchFormulaException("test message");
		assertEquals("The message is not the expected one", "test message", e.getMessage());
	}

}
