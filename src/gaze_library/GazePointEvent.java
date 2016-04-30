package gaze_library;

import java.util.EventObject;

/**
 * @author Thorsten Bövers
 * A GazePointEvent is created if a GazeDataProvider receives a GazePoint.
 * With this event, all GazePointListener of the particular GazeDataProvider are notified.
 * Each event holds the GazePoint and the GazePoint source namely the GazeDataProvider.
 */
public class GazePointEvent extends EventObject{
	
	private static final long serialVersionUID = 2422929685512401346L;
	
	/**
	 * This variable stores the GazeDataProvider as source
	 */
	private GazeDataProvider source;
	
	/**
	 * This variable stores the GazePoint
	 */
	private GazePoint gazePoint;
	
	/**
	 * Standard constructor for a GazePointEvent
	 * @param source of the GazePoint as a GazeDataProvider object
	 */
	public GazePointEvent(GazeDataProvider source) {
		super(source);
		this.source = source;
	}
	
	/**
	 * Function for getting the source of the gazePoint.
	 * @return source as GazeDataProvider object
	 */
	@Override
	public GazeDataProvider getSource() {
		return source;
	}
	
	/**
	 * Function for getting the the gazePoint.
	 * @return gazePoint as GazePoint object
	 */
	public GazePoint getGazePoint()
	{
		return gazePoint;
	}
	
	/**
	 * Function for setting the the gazePoint.
	 * @param gp as GazePoint object
	 */
	public void setGazePoint(GazePoint gp) {
		gazePoint = gp;
	}
}
