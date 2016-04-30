package gaze_selection_library;

/**
 * @author Thorsten Bövers
 * This interface represents a Presenter for displaying GazeTarget panels.
 */
public interface GazeTargetPresenter {
	/**
	 * This methods set the given GazeTarget panel to the presenter.
	 * @param targetPanel as GazeTarget object
	 */
	public void setGazeTarget(GazeTarget targetPanel);
}