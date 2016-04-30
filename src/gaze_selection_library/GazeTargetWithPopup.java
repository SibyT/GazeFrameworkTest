package gaze_selection_library;

import gaze_library.FilteredDataProvider;
import gaze_library.GazeDataProvider;

import java.util.LinkedList;

/**
 * @author Thorsten Bövers
 * This class is for gaze selection techniques that involve the use of pop-ups.
 * They can be implemented by extending this class.
 */
public abstract class GazeTargetWithPopup extends BasicGazeTarget {

	private static final long serialVersionUID = -4687728001793022150L;

	/**
	 * Standard constructor which initialized the BasicGazeTarget with standard values.
	 * @param testManager as BasicGazeTarget owner
	 * @param rawGazeDataProvider
	 * @param filteredDataProvider
	 */
	public GazeTargetWithPopup(TestManager testManager, GazeDataProvider rawGazeDataProvider, FilteredDataProvider filteredDataProvider) {
		super(testManager, rawGazeDataProvider, filteredDataProvider);
	}

	/**
	 * Constructor which initialized the BasicGazeTarget with the values given in the basicGazeTargetTypeProperties list.
	 * @param testManager as BasicGazeTarget owner
	 * @param rawGazeDataProvider
	 * @param filteredDataProvider
	 * @param basicGazeTargetTypeProperties
	 */
	public GazeTargetWithPopup(TestManager testManager, GazeDataProvider rawGazeDataProvider, FilteredDataProvider filteredDataProvider, LinkedList<String> basicGazeTargetTypeProperties) {
		super(testManager, rawGazeDataProvider, filteredDataProvider, basicGazeTargetTypeProperties);
	}

	/**
	 * This method enables to register another GazeTarget as the pop-up for this class.
	 * @param popup as a GazeTarget object
	 */
	public void setPopup(GazeTarget popup) {
		
	}
}
