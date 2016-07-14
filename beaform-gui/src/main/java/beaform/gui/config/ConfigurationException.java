package beaform.gui.config;

public class ConfigurationException extends Exception {

	private static final long serialVersionUID = -5634411096560763062L;

	public ConfigurationException() {
		super();
	}

	public ConfigurationException(final String message) {
		super(message);
	}

	public ConfigurationException(final Throwable cause) {
		super(cause);
	}

	public ConfigurationException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
