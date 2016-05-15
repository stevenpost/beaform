package beaform.gui.search;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import beaform.entities.Formula;
import beaform.gui.FormulaTree;

public final class RenderSearchResult implements Runnable {

	private final Future<Formula> searchresult;
	private final JPanel pane;

	public RenderSearchResult(Future<Formula> searchresult, JPanel pane) {
		this.searchresult = searchresult;
		this.pane = pane;
	}

	@Override
	public void run() {
		final Formula searchResult;
		try {
			searchResult = this.searchresult.get();
		}
		catch (InterruptedException | ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		SwingUtilities.invokeLater(new AddFormTreeToGui(searchResult, this.pane));
	}

	public static final class AddFormTreeToGui implements Runnable {
		private final Formula searchResult;
		private final JPanel pane;

		public AddFormTreeToGui(Formula searchResult, JPanel pane) {
			this.searchResult = searchResult;
			this.pane = pane;
		}

		@Override
		public void run() {
			FormulaTree ft = new FormulaTree(this.searchResult);
			if (this.pane.getComponentCount() > 3) {
				this.pane.remove(3);
			}
			this.pane.add(ft);
			this.pane.revalidate();
		}
	}
}