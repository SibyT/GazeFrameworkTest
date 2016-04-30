package gaze_library;

import java.awt.Point;
import java.util.logging.Logger;

/** 
 * @author Thorsten Bövers
 * This interface provides access to gazePoint data such as the location of the gazePoint
 * on the screen, the time when the gazePoint occurred, and the validity of the gazePoint.
 */

public interface GazePoint {
	
	/**
	 * The Java-Logging-Logger.
	 */
	static final Logger log = Logger.getLogger(GazePoint.class.getName());
	
	/**
	 * Function for getting the location of the gazePoint on the screen.
	 * @return two dimensional coordinates on the screen as a Point object
	 */
	public Point getLocation();
	
	/**
	 * Function for getting the validity value of the gazePoint.
	 * Validity data is represented by an enumeration value representing one of three
	 * possible values, namely VALID, PARTIALLY_VALID, and INVALID.
	 * @return validity value as Validity enumeration object
	 */
	public Validity getValidity();
	
	/**
	 * Function for getting the timestamp of the gazePoint.
	 * @return timestamp as long value of the high precision CPU time tick
	 */
	public long getTimestamp();	
}
