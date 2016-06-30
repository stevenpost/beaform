package beaform.search;

import java.util.List;
import java.util.concurrent.Callable;

import beaform.dao.FormulaDAO;
import beaform.entities.Formula;

/**
 * A search task to find formulas, based on tags assigned to them.
 *
 * @author Steven Post
 *
 */
public final class SearchFormulasByTagTask implements Callable<List<Formula>> {

	private final String tagName;

	public SearchFormulasByTagTask(final String tagName) {
		this.tagName = tagName;
	}

	@Override
	public List<Formula> call() {
		final String name = this.tagName;

		return FormulaDAO.findFormulasByTag(name);

	}
}
