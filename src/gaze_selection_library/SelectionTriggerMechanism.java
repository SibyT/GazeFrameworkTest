package gaze_selection_library;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * @author Thorsten Bövers
 * This abstract class represents basic methods for all different kinds of trigger mechanisms.
 * If a selection trigger with a specific mechanism, all observing SelectionTriggerMechanismListener are notified
 * to start their trigger selection sequence.
 */
public abstract class SelectionTriggerMechanism {
	
	/**
	 * The Java-Logging-Logger.
	 */
	static final Logger log = Logger.getLogger(SelectionTriggerMechanism.class.getName());

	/**
	 * This linkedList stores all SelectionTriggerMechanismListener, which are notified if particular trigger event happened.
	 */
	protected LinkedList<SelectionTriggerMechanismListener> _selectionTriggerMechanismListener = new LinkedList<SelectionTriggerMechanismListener>();

	/**
	 * Only if this flag is set, the SelectionTriggerMechanism should be started.
	 * It is the responsibility of all implementing SelectionTriggerMechanism to guarantee this in their methods.
	 * This flag is important to avoid unwanted trigger events.
	 */
	protected boolean isActivated = false;

	/**
	 * Empty standard constructor.
	 */
	public SelectionTriggerMechanism() {

	}

	/**
	 * This function adds the given SelectionTriggerMechanismListener to the list, such that it is registered
	 * at this SelectionTriggerMechanism and will be notified, if a trigger event happened.
	 * @param gazeTarget
	 */
	public synchronized void addSelectionTriggerMechanismListener(SelectionTriggerMechanismListener selectionTriggerMechanismListener) {
		_selectionTriggerMechanismListener.add(selectionTriggerMechanismListener);
	}

	/**
	 * This function removes the given SelectionTriggerMechanismListener from the list, so that it will no longer
	 * be notified if trigger event happened within this SelectionTriggerMechanism.
	 * @param selectionTriggerMechanismListener
	 */
	public synchronized void removeSelectionTriggerMechanismListener(SelectionTriggerMechanismListener selectionTriggerMechanismListener) {
		_selectionTriggerMechanismListener.remove(selectionTriggerMechanismListener);
	}

	/**
	 * If this function is called, all observing SelectionTriggerMechanismListener at this time are notified
	 * to call their selection trigger routines.
	 * The isActivated flag is also set to false.
	 */
	protected synchronized void notifyAllSelectionTriggerMechanismListener() {
		isActivated = false;		
		Iterator<SelectionTriggerMechanismListener> stmlIter = _selectionTriggerMechanismListener.iterator();
		TriggerEvent triggerEvent = new TriggerEvent(this);
		SelectionTriggerMechanismListener current;
		while (stmlIter.hasNext()) {
			current = stmlIter.next();
			current.triggered(triggerEvent);
		}
	}

	/**
	 * This function sets the flag to activate the SelectionTriggerMechanism.
	 */
	public synchronized void setActivated() {
		isActivated = true;
	}
	
	/**
	 * This method removes the SelectionTriggerMechanism from the registered DataProviders.
	 */
	public abstract void setDisabled();

}
