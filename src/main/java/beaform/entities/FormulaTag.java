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

	@Id
	private String name;

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
}