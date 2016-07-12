package beaform.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import beaform.gui.search.SearchGui;

public class SearchGuiLaunchAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		final SearchGui searchgui = new SearchGui();
		searchgui.addObserver(MainGUI.getInstance());
		searchgui.replace();
	}

}
