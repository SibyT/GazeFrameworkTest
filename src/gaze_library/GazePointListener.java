package gaze_library;

/**
 * @author Thorsten B�vers
 * Any class which requires gaze input from the eye tracker has to implement this interface.
 * It has got only one method, which receives a gaze point event for each gaze point
 * generated by the tracker. In order to receive these events, the GazePointListener needs
 * to be registered with a GazeDataProvider.
 */
public interface GazePointListener
{
	/**
	 * This method is called from the GazePointProvider, whenever a GazePoint happened.
	 * @param gazePointEvent contains the source as GazeDataProvider and the GazePoint itself.
	 */
	public void gazePointHappened(GazePointEvent gazePointEvent);
}
