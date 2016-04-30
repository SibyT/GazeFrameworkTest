package gaze_library;

import java.util.logging.Logger;

/**
 * @author Thorsten Bövers
 * All gaze data provided by the library is exposed via this interface.
 * All classes implementing this interface have to store GazePointListener, which have to
 * be notified if a new GazePoint within this provider happens.
 */
public interface GazeDataProvider
{	
	/**
	 * The Java-Logging-Logger.
	 */
	static final Logger log = Logger.getLogger(GazeDataProvider.class.getName());
	
	/**
	 * This function calls necessary methods for closing all used threads, buffers,...
	 * @throws Exception 
	 */
	public void closeGazeDataProvider() throws Exception;
	
	/**
	 * This function adds the given GazePointListener into the list, such that it is registered
	 * at this GazeDateProvider and will be notified, if a GazePoint happened.
	 * @param gpListener
	 */
	public void addGazePointListener (GazePointListener gpListener);
	
	/**
	 * This function removes the given GazePointListener from the list, so that it will no longer
	 * be notified if a GazePoint happened within this GazeDataProvider.
	 * @param gpListener
	 */
	public void removeGazePointListener (GazePointListener gpListener);
	
	/**
	 * This method returns the tracking frequency
	 * @return the trackerFrequency
	 */
	public int getTrackerFrequency();
}