package beaform.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Tag {

	@Id
	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
