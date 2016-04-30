package gaze_library;

import java.awt.Point;

public class TheEyeTribeGazePoint implements GazePoint{
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
	public TheEyeTribeGazePoint(int pogx, int pogy, int validity, long timestamp) {
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

	@Override
	public Point getLocation() {
		// TODO Auto-generated method stub
		return location;
	}

	@Override
	public Validity getValidity() {
		// TODO Auto-generated method stub
		return validity;
	}

	@Override
	public long getTimestamp() {
		// TODO Auto-generated method stub
		return timestamp;
	}

}
