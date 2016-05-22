package beaform.gui.search;

/**
 * This enum represents a search type.
 *
 * @author Steven Post
 *
 */
public enum SearchType {
	/** Tag */
	TAG("Tag"),
	/** Formula */
	FORMULA("Formula");

	/** A display string. */
	private final String display;

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
