package beaform.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.gui.InterchangableWindowDisplayer;
import beaform.gui.search.SearchGui;

public class SearchGuiLaunchAction implements ActionListener {

	private final InterchangableWindowDisplayer icwd;

	public SearchGuiLaunchAction(final InterchangableWindowDisplayer icwd) {
		this.icwd = icwd;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final SearchGui searchgui = new SearchGui();
		searchgui.addObserver(this.icwd);
		searchgui.replace();
	}

}
