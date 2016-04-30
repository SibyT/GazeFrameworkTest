package gaze_selection_library;

import java.util.LinkedList;
import java.util.logging.Logger;

import gaze_library.GazePoint;

/**
 * @author Thorsten Bövers
 * Objects of this interface provide access to the raw and filtered gaze points that will occur after the trigger has been pressed.
 * This data is intended for use in cases where researchers would like to reduce the number of early trigger errors.
 */
public interface GazeDataFuture {
	
	/**
	 * The Java-Logging-Logger.
	 */
	static final Logger log = Logger.getLogger(GazeDataFuture.class.getName());
	
	/**
	 * This function returns a LinkedList which contains all FutureGazePoints.
	 * @return LinkedList with GazePoint objects
	 */
	public LinkedList<GazePoint> getFutureGazePoints();

	/**
	 * Because of implementing classes run in a background thread, this function allows to stop listening in this thread.
	 * This method also sets the listening flag to the given value and registers the implementing FutureGazeReader at the given GazeDataProvider.
	 * @param value flag to set if to listen to the GazeDataProvider
	 */
	public void setIsListening(boolean value);
}
