package gaze_selection_library;

/**
 * @author Thorsten B�vers
 * Any class which want's to get notified if a class implementing a SelectionTriggerMechanism generates a TriggerEvent
 * has to implement this interface. In order to receive these events, the SelectionTriggerMechanismListener has to be
 * registered with a SelectionTriggerMechanism.
 */
public interface SelectionTriggerMechanismListener {

	/**
	 * This method is called from the SelectionTriggerMechanism whenever a TriggerEvent happened.
	 * @param te which contains the source of the TriggerEvent
	 */
	public void triggered(TriggerEvent te);
	
}

