package gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class StateChanger {
	private final List<ChangeListener> cls = new ArrayList<>();

	/**
	 * Adds a ChangeListener that is only invoked after adjusting of the value.
	 * 
	 * @param cl
	 *            listener
	 */
	public void addChangeListener(ChangeListener cl) {
		this.cls.add(cl);
	}

	/**
	 * Removes the specified ChangeListener, if contained.
	 * 
	 * @param cl
	 *            the listener
	 * @return true, if the listener was contained
	 */
	public boolean removeChangeListener(ChangeListener cl) {
		return this.cls.remove(cl);
	}

	protected void notifyListeners(ChangeEvent e) {
		for (ChangeListener cl : this.cls) {
			cl.stateChanged(e);
		}
	}
}
