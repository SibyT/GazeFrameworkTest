package gaze_library;

/**
 * @author Thorsten Bövers
 * Enumeration with special types for the validity of a GazePoint.
 * Valid means that both eyes are VALID, PARTIALLY_VALID, if only one eye is valid and INVALID if both eyes are not gathered correctly.
 */
public enum Validity
{
	VALID,
	PARTIALLY_VALID,
	INVALID	
}