package beaform.importer;

public class ImporterException extends Exception {

	private static final long serialVersionUID = -3373350132920497986L;

	public ImporterException(final String message) {
		super(message);
	}

	public ImporterException(final Throwable cause) {
		super(cause);
	}

	public ImporterException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
