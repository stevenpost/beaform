package beaform.entities;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class BaseCompound {

	private final String name;
	private String description = "";

	public BaseCompound(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj != null && this.getClass() == obj.getClass()) {
			final BaseCompound testTag = (BaseCompound) obj;
			return this.name.equals(testTag.name) && this.description.equals(testTag.description);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
						append(this.name).
						append(this.description).
						toHashCode();
	}
}
