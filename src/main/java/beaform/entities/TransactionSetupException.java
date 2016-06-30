package beaform.entities;

/**
 * This exception indicates a failure in setting up a transaction.
 *
 * @author Steven Post
 *
 */
public class TransactionSetupException extends Exception {

	private static final long serialVersionUID = 1L;

	public TransactionSetupException() {
		super();
	}

	public TransactionSetupException(final String message) {
		super(message);
	}

	public TransactionSetupException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
