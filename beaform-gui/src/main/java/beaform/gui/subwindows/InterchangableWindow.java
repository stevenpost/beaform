package beaform.gui.subwindows;

import java.util.Observer;

public interface InterchangableWindow {

	void replace();

	void addObserver(final Observer o);

}
