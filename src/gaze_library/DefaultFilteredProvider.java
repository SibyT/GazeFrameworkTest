package gaze_library;

/**
 * @author Thorsten Bövers
 * This class is used to support gaze data filtering support. It implements the FilteredDataProvider interface.
 * One example can be the smoothing algorithm of Kumar (2007).
 */
public class DefaultFilteredProvider implements FilteredDataProvider {

	/**
	 * Standard constructor
	 */
	public DefaultFilteredProvider() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see gaze_library.GazeDataProvider#addGazePointListener(gaze_library.GazePointListener)
	 */
	@Override
	public void addGazePointListener(GazePointListener listener) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see gaze_library.GazeDataProvider#removeGazePointListener(gaze_library.GazePointListener)
	 */
	@Override
	public void removeGazePointListener(GazePointListener listener) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see gaze_library.GazeDataProvider#closeGazeDataProvider()
	 */
	@Override
	public void closeGazeDataProvider() throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see gaze_library.GazeDataProvider#getTrackerFrequency()
	 */
	@Override
	public int getTrackerFrequency() {
		// TODO Auto-generated method stub
		return 0;
	}

}
