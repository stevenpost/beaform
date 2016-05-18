package beaform.gui.search;

/**
 * This enum represents a search type.
 *
 * @author Steven Post
 *
 */
public enum SearchType {
	TAG("Tag"),
	FORMULA("Formula");

	/** A display string. */
	private final String display;

	/**
	 * A private constructor.
	 * @param display the display name for the enum
	 */
	private SearchType(final String display) {
		this.display = display;
	}

	/**
	 * @return the display name
	 */
	@Override
	public String toString() {
		return this.display;
	}
}
