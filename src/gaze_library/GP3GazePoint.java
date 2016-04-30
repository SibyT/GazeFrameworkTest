package gaze_library;

import java.awt.Point;

/**
 * @author Thorsten Bövers
 * This class maps gazePoint data from the particular tracker, in this case the GP3 
 * eye tracker, to the GazePoint interface.
 */
public class GP3GazePoint implements GazePoint {
	
	private Point location = new Point();
	private Validity validity;
	private long timestamp;
	
	/**
	 * Standard constructor
	 * @param x coordinate of the gazePoint on the screen
	 * @param y coordinate of the gazePoint on the screen
	 * @param validity value of the gazePoint
	 * @param timestamp when the gazePoint happened
	 */
	public GP3GazePoint(int pogx, int pogy, int validity, long timestamp) {
		location.x = pogx;
		location.y = pogy;
		if (validity == 2) {
			this.validity = Validity.VALID;
		}
		else if (validity == 1) {
			this.validity = Validity.PARTIALLY_VALID;
		} else {
			this.validity = Validity.INVALID;
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
