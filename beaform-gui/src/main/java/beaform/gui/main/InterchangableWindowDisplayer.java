package beaform.gui.main;

import java.awt.Component;
import java.util.Observer;

public interface InterchangableWindowDisplayer extends Observer {

	void replaceActiveWindow(final Component comp);
}
