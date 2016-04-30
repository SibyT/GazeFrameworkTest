package gaze_selection_library;

import java.util.EventObject;

/**
 * @author Thorsten Bövers
 * A TriggerEvent is created by a SelectionTriggerMechanism.
 * With this event, all SelectionTriggerMechansimListener of the particular SelectionTriggerMechanism are notified.
 * Each event holds the source namely the SelectionTriggerMechanism.
 */
public class TriggerEvent extends EventObject {

	private static final long serialVersionUID = 6141452003100683772L;
	
	/**
	 * This variable stores the GazeDataProvider as source
	 */
	private SelectionTriggerMechanism source;

	/**
	 * Standard constructor for a TriggerEvent
	 * @param source of the TriggerEvent as a SelectionTriggerMechanism object
	 */
	public TriggerEvent(SelectionTriggerMechanism source) {
		super(source);
		this.source = source;
	}

	/**
	 * Function for getting the source of the TriggerEvent.
	 * @return source as SelectionTriggerMechanism object
	 */
	@Override
	public SelectionTriggerMechanism getSource() {
		return source;
	}

}
