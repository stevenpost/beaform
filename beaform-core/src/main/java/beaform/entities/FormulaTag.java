package beaform.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This class represents a tag.
 *
 * @author steven
 *
 */
@Entity
public class FormulaTag implements Serializable {

	private static final long serialVersionUID = 8568943568088422588L;

	@Id
	private String name;

	public FormulaTag() {
		// Default constructor for Hibernate.
	}

	public FormulaTag(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
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
			final FormulaTag testTag = (FormulaTag) obj;
			return this.name.equals(testTag.name);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
						append(this.name).
						toHashCode();
	}

}
