package gaze_library;

import java.awt.Point;

/**
 * @author Thorsten Bövers
 * This class represents a basic implementation of the GazePoint interface
 * that is used for creating points for simulated or filtered gaze data.
 */
public class SimpleGazePoint implements GazePoint {
	
	private Point location = new Point();
	private Validity validity;
	private long timestamp;
	
	/**
	 * Standard constructor
	 * @param x coordinate of the gazePoint on the screen
	 * @param y coordinate of the gazePoint on the screen
	 * @param validity value of the gazePoint
	 * @param timestamp when the gazePoint happened as CPU time tick
	 */
	public SimpleGazePoint(int x, int y, int validity, long timestamp) {
		location.x = x;
		location.y = y;
		switch (validity) {
		case 0:
			this.validity = Validity.INVALID;
			break;
		case 1:
			this.validity = Validity.PARTIALLY_VALID;
			break;
		case 2:
			this.validity = Validity.VALID;
			break;
		}
		this.timestamp = timestamp;		
	}

	/* (non-Javadoc)
	 * @see gaze_library.GazePoint#getLocation()
	 */
	@Override
	public Point getLocation() {
		return location;
	}

	/* (non-Javadoc)
	 * @see gaze_library.GazePoint#getValidity()
	 */
	@Override
	public Validity getValidity() {
		return validity;
	}

	/* (non-Javadoc)
	 * @see gaze_library.GazePoint#getTimestamp()
	 */
	@Override
	public long getTimestamp() {
		return timestamp;
	}

}
