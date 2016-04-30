package gaze_selection_library;

import gaze_library.FilteredDataProvider;
import gaze_library.GazeDataProvider;
import gaze_library.GazePoint;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComponent;

import org.jdom2.Element;

/**
 * @author Thorsten Bövers
 * A gaze selection technique is represented by a GazeTarget.
 * Every selection technique is implemented by extending this class either directly or indirectly.
 * This class extends JComponent and represents a panel for displaying target items on the screen.
 * A GazeTarget object can be used either as a stand-alone technique, or as a pop-up.
 */
public abstract class GazeTarget extends JComponent {

	private static final long serialVersionUID = 333493295053202340L;
	
	/**
	 * The Java-Logging-Logger.
	 */
	static final Logger log = Logger.getLogger(GazeTarget.class.getName());
	
	/**
	 * Stores a reference to the unfiltered GazeDataProvider and is set within the constructor.
	 */
	protected GazeDataProvider rawGazeDataProvider = null;
	
	/**
	 * Stores a reference to the filtered GazeDataProvider and is set within the constructor.
	 */
	protected FilteredDataProvider filteredDataProvider = null;
	
	/**
	 * Stores a reference to the GazeTarget owner, in this implementation the TestManager object.
	 * The TestManager is notified if new test data is recorded and if the selection process is done.
	 * Because then the TestManager can select the next intended target item.
	 */
	protected TestManager gazeTargetOwner = null;
	
	/**
	 * This variable stores the particular tracking frequency of the eye tracker and is set within the constructor.
	 */
	protected int trackerFrequency = 0;
	
	/**
	 * Standard constructor which stores the referenced objects.
	 * @param testManager
	 * @param rawGazeDataProvider
	 * @param filteredDataProvider
	 */
	public GazeTarget(TestManager testManager, GazeDataProvider rawGazeDataProvider, FilteredDataProvider filteredDataProvider) {
		this.rawGazeDataProvider = rawGazeDataProvider;
		this.filteredDataProvider = filteredDataProvider;
		this.gazeTargetOwner = testManager;
		
		if (rawGazeDataProvider!=null) {
			trackerFrequency = rawGazeDataProvider.getTrackerFrequency();
		} else if (filteredDataProvider!=null){
			trackerFrequency = filteredDataProvider.getTrackerFrequency();
		}	
	}

	/**
	 * This method calls the selection sequence and is called if a selection is triggered
	 */
	protected abstract void selectionTriggered();
	
	/**
	 * This method is called whenever a selection is triggered.
	 * It is the responsibility of this method to determine the target item to select when the trigger is pressed.
	 * @param recentLocation of the most recent filtered gaze point
	 * @param _rawGazePoints of the raw (unfiltered) GazePoint instances preceding the trigger point
	 * @param _filteredGazePoints of the filtered GazePoint instances preceding the trigger point
	 * @param rawGazeDataFuture GazeDataFuture object which stores the unfiltered GazePoint instances which happened after the selection is triggered
	 * @param filteredGazeDataFuture GazeDataFuture object which stores the filtered GazePoint instances which happened after the selection is triggered
	 */
	protected abstract void resolveSelection(Point recentLocation, ConcurrentLinkedDeque<GazePoint> _rawGazePoints, ConcurrentLinkedDeque<GazePoint> _filteredGazePoints, GazeDataFuture rawGazeDataFuture, GazeDataFuture filteredGazeDataFuture);

	/**
	 * This method is called after the target selection has been resolved by the resolveSelection method.
	 * Its action typically results in the item selected being shown as selected.
	 * @param button which should be depressed
	 */
	//protected abstract void doSelection(JButton button);
	protected abstract void doSelection(JButton button);


	/**
	 * This method returns an XML representation which consists of all sufficient properties.
	 * @return a XML jDom element with GazeTarget properties
	 */
	protected abstract Element toXML();

	/**
	 * This method is used to give the user an indicator which target item should be selected.
	 * It notifies this GazeTarget that a particular target item should be highlighted.
	 * @param index of the particular target item to select
	 */
	protected abstract void setTargetIndex(int index);

	/**
	 * This method returns a rectangle representing the bounds of the currently highlighted target.
	 * @return a rectangle representing the bounds of the currently highlighted target
	 */
	protected abstract Rectangle targetRect();

	/**
	 * The setPopupOwner method is called to register a GazeTarget as an owner of a pop-up.
	 * GazeTargets can be used as pop-ups for other GazeTargets (usually a GazeTargetWithPopup instance).
	 * In this manner a gaze selection technique is implemented as a combination of two GazeTargets.
	 * One acts as the owner, and the other the pop-up.
	 * This method sets the owner value of a GazeTarget, preparing it for use as a pop-up.
	 */
	protected abstract void setPopupOwner();

	/**
	 * The initLocation method sets the location of the pop-up on the screen based on this location and
	 * performs any other initialization tasks required before the pop-up can be displayed.
	 * A pop-up must appear in the correct location when triggered.
	 * This is typically based on the location last filtered point (fixation).
	 */
	protected abstract void initLocation();

	/**
	 * When this method is called, a screen shot is captured.
	 * Screen shots, along with gaze data are required for later analysis of important events.
	 * This occurs by default for selections and gaze pop-ups.
	 * Some techniques may require data and screen captures for other important events.
	 */
	protected abstract void takeScreenshot();

}
