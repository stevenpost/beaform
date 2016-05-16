package beaform.gui.formulaeditor;

import java.util.Comparator;

import beaform.entities.Tag;

/**
 * This class can compare Tag objects.
 *
 * @author steven
 *
 */
public class TagComparator implements Comparator<Tag> {

	@Override
	public int compare(Tag o1, Tag o2) {
		return o1.getName().compareTo(o2.getName());
	}

}