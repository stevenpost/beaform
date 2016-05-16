package beaform.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Tag implements Serializable { // NOPMD by steven on 5/16/16 4:26 PM

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
