package beaform.gui;

import java.awt.Component;

@FunctionalInterface
public interface InterchangableWindowDisplayer {

	void replaceActiveWindow(final Component comp);
}
