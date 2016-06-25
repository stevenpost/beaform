package beaform;

import org.junit.Test;

import beaform.entities.TransactionSetupException;
import junit.framework.TestCase;

/**
 * Test the formula entity.
 * @author Steven Post
 *
 */
public class TransactionExceptionTest extends TestCase {

	/**
	 * Check the message.
	 */
	@Test
	public void testMessage() {
		final Throwable testException = new TransactionSetupException("Transaction setup failed");
		assertEquals("This isn't the expected message", "Transaction setup failed", testException.getMessage());
	}

	/**
	 * Check the cause.
	 */
	@Test
	public void testCause() {
		final Throwable testException = new TransactionSetupException("Transaction setup failed", new Exception("This is the cause"));
		assertEquals("This isn't the expected cause", "This is the cause", testException.getCause().getMessage());
	}

}
