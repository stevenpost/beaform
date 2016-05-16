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

	/**
	 *
	 */
	private static final long serialVersionUID = 6949518937805857985L;

	@Override
	public int compare(final FormulaTag firstTag, final FormulaTag secondTag) {
		return firstTag.getName().compareTo(secondTag.getName());
	}

}