package gaze_selection_library;

import gaze_library.FilteredDataProvider;
import gaze_library.GazeDataProvider;
import gaze_library.GazePoint;

import java.awt.Color;
import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.swing.JButton;

/**
 * @author Thorsten Bövers
 * This class extends the BasicGazeTarget class by using a gesture which has to be performed after a targetItem was selected.
 * This is one solution for the Midas Touch Problem and works as follows:
 * At first one of the targetItems has to be selected (Feedback: Buttontext marked red). If this is done the GestureMode is activated.
 * In this mode it is also possible to select another targetItem.
 * If the gesture is performed the button which was selected before is triggered.
 * On the screenshot the GazePoints for the gesture are marked as future GazePoints.
 * The gesture can be specified in the GestureTriggerMechanism class.
 */
public class BasicGazeTargetWithGesture extends BasicGazeTarget {

	private static final long serialVersionUID = 5741638080543763585L;

	/**
	 * This variable stores the index of the marked targetItem.
	 */
	protected int markedTargetItemIndex = -1;

	/**
	 * This variable stores a reference to the GestureTriggerMechansim.
	 */
	protected GestureTriggerMechanism gestureTriggerMechanism;

	/**
	 * This flag is set after a targetItem has be selected.
	 */
	protected boolean gestureModeIsActivated = false;

	/**
	 * This flag is set if the gesture was performed.
	 */
	protected boolean gesturePerformed = false;

	/**
	 * Standard constructor which initialized the BasicGazeTarget with standard values and initialized the GestureTriggerMechanism.
	 * @param testManager as BasicGazeTarget owner
	 * @param rawGazeDataProvider
	 * @param filteredDataProvider
	 */
	public BasicGazeTargetWithGesture(TestManager testManager, GazeDataProvider rawGazeDataProvider,	FilteredDataProvider filteredDataProvider) {
		super(testManager, rawGazeDataProvider, filteredDataProvider);

		// instantiate GestureTriggerMechanism
		if (rawGazeDataProvider != null) {
			gestureTriggerMechanism = new GestureTriggerMechanism(rawGazeDataProvider, this,200,1000);
		} else if (filteredDataProvider != null) {
			gestureTriggerMechanism = new GestureTriggerMechanism(filteredDataProvider, this,200,1000);			
		}
	}

	/**
	 * Constructor which initialized the BasicGazeTarget with the values given in the basicGazeTargetTypeProperties list
	 * and initialized the GestureTriggerMechanism.
	 * @param testManager as BasicGazeTarget owner
	 * @param rawGazeDataProvider
	 * @param filteredDataProvider
	 * @param basicGazeTargetTypeProperties
	 */
	public BasicGazeTargetWithGesture(TestManager testManager, GazeDataProvider rawGazeDataProvider,	FilteredDataProvider filteredDataProvider, LinkedList<String> basicGazeTargetTypeProperties) {
		super(testManager, rawGazeDataProvider, filteredDataProvider, basicGazeTargetTypeProperties);

		// instantiate GestureTriggerMechanism
		if (rawGazeDataProvider != null) {
			gestureTriggerMechanism = new GestureTriggerMechanism(rawGazeDataProvider, this,200,1000);
		} else if (filteredDataProvider != null) {
			gestureTriggerMechanism = new GestureTriggerMechanism(filteredDataProvider, this,200,1000);			
		}
	}

	/* (non-Javadoc)
	 * @see gaze_selection_library.GazeTarget#selectionTriggered()
	 */
	@Override
	protected void selectionTriggered() {

		if (gesturePerformed) {
			selectionTriggerMechanism.setDisabled();
			log.info(System.currentTimeMillis() + " - " + "Stopped the SelectionTriggerMechanism");
			gesturePerformed = false;
			// check if gesture mode was activated (should be the case!)
			if (gestureModeIsActivated) {

				gestureModeIsActivated = false;

				// end recording time
				timeEnd = System.currentTimeMillis();
				timeTickTriggered = System.nanoTime();
				log.info(System.currentTimeMillis() + " - " + "Stopped recording time");

				// remove this SelectionTriggerMechanismListener from the GestureSelectionTriggerMechanism
				gestureTriggerMechanism.removeSelectionTriggerMechanismListener(this);
				log.info(System.currentTimeMillis() + " - " + "Removed BasicGazeTargetWithGesture as SelectionTriggerMechanismListener from the GestureTriggerMechanism");


				// remove this GazePointListener from the GazeDataProvider
				if (rawGazeDataProvider!=null) { 
					rawGazeDataProvider.removeGazePointListener(this);
				}
				if (filteredDataProvider!=null) { 
					filteredDataProvider.removeGazePointListener(this);
				}
				log.info(System.currentTimeMillis() + " - " + "Removed GazeTargetPanel as GazePointListener from the GazeDataProvider");

				// remove this SelectionTriggerMechanismListener from the SelectionTriggerMechanism		
				selectionTriggerMechanism.removeSelectionTriggerMechanismListener(this);
				log.info(System.currentTimeMillis() + " - " + "Removed GazeTargetPanel as SelectionTriggerMechanismListener from the SelectionTriggerMechanism");

				doSelection(_targetItems.get(selectedTargetItemIndex));
				log.info(System.currentTimeMillis() + " - " + "Performed visual click on selected ScreenButton");

				log.info(System.currentTimeMillis() + " - " + "Start to take screenshots");
				takeScreenshot();
				log.info(System.currentTimeMillis() + " - " + "Taking screenshots finished");

				// unmark last trigger targetItem
				selectTriggeredTargetItem(_targetItems.get(markedTargetItemIndex), false);
				markedTargetItemIndex = -1;

				// unmark target item
				_targetItems.get(intendedTargetItemIndex).setBackground(targetItemColor);

				// compute time needed to trigger button
				timeDuration = timeEnd - timeStart;

				log.info(System.currentTimeMillis() + " - " + "Start writing data to the XML element of current TestPlanItem");
				recordData(intendedTargetItemIndex,selectedTargetItemIndex,timeDuration,_rawGazePoints,_filteredGazePoints,this.rawGazeDataFuture,this.filteredGazeDataFuture);
				log.info(System.currentTimeMillis() + " - " + "Finished writing data to the XML element of current TestPlanItem");

				targetIndexCounter++;

				// notify BasicGazeTargetOwner (TestManager), that the user performed the click and recording data is finished
				// because now, the owner can select the next intended target item
				// send recorded data to the GazeTarget owner too
				gazeTargetOwner.recordingDataInGazeTargetFinished(recordedData);
			} else {
				// this should not be the case
				log.info(System.currentTimeMillis() + " - " + "Error in BasicGazeTargetWithGesture: Unexpected state");
			}	
		} else {
			// compute recent location of the last gazePoints
			computeRecentLocation();

			resolveSelection(recentLocation, _rawGazePoints, _filteredGazePoints, rawGazeDataFuture, filteredGazeDataFuture);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see gaze_selection_library.GazeTarget#resolveSelection(java.awt.Point, java.util.concurrent.ConcurrentLinkedDeque, java.util.concurrent.ConcurrentLinkedDeque, gaze_selection_library.GazeDataFuture, gaze_selection_library.GazeDataFuture)
	 */
	@Override
	protected void resolveSelection(Point recentLocation, ConcurrentLinkedDeque<GazePoint> rawGPs, ConcurrentLinkedDeque<GazePoint> filteredGPs, GazeDataFuture rawGazeDataFuture, GazeDataFuture filteredGazeDataFuture) {
		// check if the given recentLocation fits to one of the targetItems
		Iterator<JButton> iter = _targetItems.iterator();
		for (int i=0; iter.hasNext(); i++) {
			JButton current = iter.next();
			if (recentLocation.x >= current.getLocation().x && recentLocation.x <= current.getLocation().x + current.getWidth() && recentLocation.y >= current.getLocation().y && recentLocation.y <= current.getLocation().y + current.getHeight()) {
				selectedTargetItemIndex = i;
				log.info(System.currentTimeMillis() + " - " + "Found selected TargetItem");
				// unmark last trigger targetItem
				if (markedTargetItemIndex != -1) {
					selectTriggeredTargetItem(_targetItems.get(markedTargetItemIndex), false);
				}
				// mark new trigger targetItem
				selectTriggeredTargetItem(_targetItems.get(i), true);
				log.info(System.currentTimeMillis() + " - " + "Set selected TargetItem as selected");
				markedTargetItemIndex = i;
				// activate gesture mode if it was not already started
				if (!gestureModeIsActivated) {
					gestureModeIsActivated = true;
					gestureTriggerMechanism.setActivated();	
					log.info(System.currentTimeMillis() + " - " + "Activated the GestureTriggerMechanism");
					gestureTriggerMechanism.addSelectionTriggerMechanismListener(this);
					log.info(System.currentTimeMillis() + " - " + "Added BasicGazeTargetWithGesture as SelectionTriggerMechanismListener to the GestureTriggerMechanism");
				}
				break;
			}
		}
		// activate the selectionTriggerMechanism again
		selectionTriggerMechanism.setActivated();
		log.info(System.currentTimeMillis() + " - " + "Activated the SelectionTriggerMechanism again");
	}

	/*
	 * (non-Javadoc)
	 * @see gaze_selection_library.BasicGazeTarget#triggerEventHappend(gaze_selection_library.TriggerEvent)
	 */
	@Override
	public void triggered(TriggerEvent triggerEvent) {
		// if trigger event was generated by the GestureTriggerMechanism -> gesture was proceeded -> set flag
		if (triggerEvent.getSource() instanceof GestureTriggerMechanism) {
			gesturePerformed = true;
		} else {
			// start to store future GazePoints
			if (storeFutureGazeData) {			
				if (rawGazeDataFuture != null) {
					rawGazeDataFuture.setIsListening(true);
				}
				if (filteredGazeDataFuture != null) {
					filteredGazeDataFuture.setIsListening(true);
				}	
				log.info(System.currentTimeMillis() + " - " + "Started recording future gaze data");
			}
		}
		synchronized (triggerThread) {
			triggerThread.notify();
		}
	}

	/**
	 * This method sets the text of the given JButton as marked in red.
	 * @param button to set textcolor
	 * @param value
	 */
	protected void selectTriggeredTargetItem(JButton button, boolean value) {
		if (value) {
			button.setForeground(Color.RED);
		} else {
			button.setForeground(Color.BLACK);
		}		
	}

}
