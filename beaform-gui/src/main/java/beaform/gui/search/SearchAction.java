package beaform.gui.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchAction implements ActionListener {

	private final SearchGui searchgui;

	public SearchAction(final SearchGui searchgui) {
		this.searchgui = searchgui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.searchgui.search();
	}

}
