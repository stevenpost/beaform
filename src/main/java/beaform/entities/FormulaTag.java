package beaform.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * This class represents a tag.
 *
 * @author steven
 *
 */
@Entity
public class FormulaTag implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8568943568088422588L;

	/**
	 * The name of this tag.
	 */
	@Id
	private String name;

	/**
	 * Constructor.
	 * @param name The name for this tag.
	 */
	public FormulaTag(final String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.name;
	}
}
