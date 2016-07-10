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

	private final String displayName;

	private SearchType(final String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return this.displayName;
	}
}
