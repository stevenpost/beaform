package beaform.entities;

/**
 * This exception indicates a failure in setting up a transaction.
 * @author Steven Post
 *
 */
public class TransactionSetupException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public TransactionSetupException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the message for this exception.
	 */
	public TransactionSetupException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param message the message for this exception
	 * @param cause the cause of this exception
	 */
	public TransactionSetupException(String message, Throwable cause) {
		super(message, cause);
	}

}
