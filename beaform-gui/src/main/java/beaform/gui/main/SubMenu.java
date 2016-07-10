package beaform.gui.main;

import javax.swing.JMenuBar;

@FunctionalInterface
public interface SubMenu {

	void attachToMenuBar(final JMenuBar menuBar);
}
