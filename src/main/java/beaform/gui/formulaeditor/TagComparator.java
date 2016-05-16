package beaform.gui.formulaeditor;

import java.io.Serializable;
import java.util.Comparator;

import beaform.entities.Tag;

/**
 * This class can compare Tag objects.
 *
 * @author steven
 *
 */
public class TagComparator implements Comparator<Tag>, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6949518937805857985L;

	@Override
	public int compare(final Tag firstTag, final Tag secondTag) {
		return firstTag.getName().compareTo(secondTag.getName());
	}

}