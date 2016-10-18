package beaform.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

@SuppressWarnings("static-method")
public class NoSuchFormulaExceptionTest {

	@Test
	public void testMessageConstructor() {
		Exception e = new NoSuchFormulaException("test message");
		assertEquals("The message is not the expected one", "test message", e.getMessage());
	}

}
