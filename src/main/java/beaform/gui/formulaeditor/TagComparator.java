package beaform.gui.formulaeditor;

import java.io.Serializable;
import java.util.Comparator;

import beaform.entities.FormulaTag;

/**
 * This class can compare Tag objects.
 *
 * @author steven
 *
 */
public class TagComparator implements Comparator<FormulaTag>, Serializable {

	/** A serial */
	private static final long serialVersionUID = 6949518937805857985L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(final FormulaTag firstTag, final FormulaTag secondTag) {
		return compareNames(firstTag.getName(), secondTag.getName());
	}

	private int compareNames(final String firstString, final String secondString) {
		return firstString.compareTo(secondString);
	}

}
