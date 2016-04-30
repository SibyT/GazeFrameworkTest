package gaze_selection_library;

import gaze_library.DummyGazeProvider;
import gaze_library.FilteredDataProvider;
import gaze_library.GazeDataProvider;
import gaze_library.GazePoint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author Thorsten Bövers
 * This class extends the BasicGazeTarget class by using an additional screen button which has to be clicked after a targetItem was selected.
 * This is one solution for the Midas Touch Problem and works as follows:
 * At first one of the targetItems has to be selected (Feedback: Buttontext marked red). If this is done the ScreenButtonMode is activated.
 * In this mode it is also possible to select another targetItem.
 * If the screen button was clicked, the TargetItem which was selected before is triggered.
 * On the screenshot all GazePoints after triggering the last TargetItem are painted as future GazePoints.
 */
public class BasicGazeTargetWithScreenButton extends BasicGazeTarget {
	
	private static final long serialVersionUID = -2361802365953536207L;

	/**
	 * This variable stores the additional screen button as JButton object.
	 */
	protected JButton screenButton;

	/**
	 * This variable stores the index of the marked targetItem.
	 */
	protected int markedTargetItem = -1;

	/**
	 * This flag is set after a targetItem has be selected.
	 */
	protected boolean screenButtonModeIsActivated = false;
	
	/**
	 * Stores the JPanel with the TargetItems.
	 */
	private JPanel targetItemsPanel;
	
	/**
	 * This variable stores a copy of the filtered location of the GazePoints which are used to indicate the trigger position.
	 * It is used for taking the screenshots.
	 */
	protected Point recentLocationCopy;
	
	/**
	 * This list stores a copy of all valid GazePoints which were used to compute the recent location for triggering the TargetItem.
	 * It is used for taking the screenshots.
	 */
	protected LinkedList<GazePoint> _validGPbeforeTriggerCopy;	

	/**
	 * Standard constructor which initialized the BasicGazeTarget with standard values.
	 * @param testManager as BasicGazeTarget owner
	 * @param rawGazeDataProvider
	 * @param filteredDataProvider
	 */
	public BasicGazeTargetWithScreenButton(TestManager testManager, GazeDataProvider rawGazeDataProvider, FilteredDataProvider filteredDataProvider) {
		super(testManager, rawGazeDataProvider, filteredDataProvider);
	}

	/**
	 * Constructor which initialized the BasicGazeTarget with the values given in the basicGazeTargetTypeProperties list.
	 * @param testManager as BasicGazeTarget owner
	 * @param rawGazeDataProvider
	 * @param filteredDataProvider
	 * @param basicGazeTargetTypeProperties
	 */
	public BasicGazeTargetWithScreenButton(TestManager testManager, GazeDataProvider rawGazeDataProvider, FilteredDataProvider filteredDataProvider, LinkedList<String> basicGazeTargetTypeProperties) {
		super(testManager, rawGazeDataProvider, filteredDataProvider, basicGazeTargetTypeProperties);
	}

	/**
	 * 
	 */
	@Override
	protected void initJPanel() {

		targetItemsPanel = new JPanel();
		targetItemsPanel.setLayout(new BoxLayout(targetItemsPanel, BoxLayout.Y_AXIS));


		for (int i=1; i<=numberOfTargetItems; i++) {
			targetItemsPanel.add(Box.createRigidArea(new Dimension(0,deadSpace)));
			JButton button = new JButton("Button "+i);
			button.setFocusable(false);
			button.setRolloverEnabled(false);
			button.setBackground(targetItemColor);
			_targetItems.addLast(button);
			button.setMaximumSize(new Dimension(7*buttonHeightRatio, 2*buttonHeightRatio));
			button.setAlignmentX(Component.CENTER_ALIGNMENT);
			// to handle simulated wink trigger with a clicked mouse also if mouse is on a button
			if (rawGazeDataProvider instanceof DummyGazeProvider) {
				button.addMouseListener((DummyGazeProvider) rawGazeDataProvider);
			} else if (filteredDataProvider instanceof DummyGazeProvider) {
				button.addMouseListener((DummyGazeProvider) filteredDataProvider);
			}
			targetItemsPanel.add(button);
		}

		this.setLayout(new BorderLayout());

		// add screen button
		screenButton = new JButton("ScreenButton");
		screenButton.setFocusable(false);
		screenButton.setRolloverEnabled(false);
		screenButton.setPreferredSize(new Dimension(4*buttonHeightRatio, 2*buttonHeightRatio));
		screenButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		// to handle simulated wink trigger with a clicked mouse also if mouse is on a button
		if (rawGazeDataProvider instanceof DummyGazeProvider) {
			screenButton.addMouseListener((DummyGazeProvider) rawGazeDataProvider);
		} else if (filteredDataProvider instanceof DummyGazeProvider) {
			screenButton.addMouseListener((DummyGazeProvider) filteredDataProvider);
		}
		this.add(screenButton,BorderLayout.LINE_START);
		this.add(targetItemsPanel,BorderLayout.CENTER);		

	}

	/* (non-Javadoc)
	 * @see gaze_selection_library.GazeTarget#selectionTriggered()
	 */
	@Override
	protected void selectionTriggered() {
		// compute recent location of the last gazePoints
		computeRecentLocation();

		resolveSelection(recentLocation, _rawGazePoints, _filteredGazePoints, rawGazeDataFuture, filteredGazeDataFuture);
	}

	/*
	 * (non-Javadoc)
	 * @see gaze_selection_library.GazeTarget#resolveSelection(java.awt.Point, java.util.concurrent.ConcurrentLinkedDeque, java.util.concurrent.ConcurrentLinkedDeque, gaze_selection_library.GazeDataFuture, gaze_selection_library.GazeDataFuture)
	 */
	@Override
	protected void resolveSelection(Point recentLocation, ConcurrentLinkedDeque<GazePoint> rawGPs, ConcurrentLinkedDeque<GazePoint> filteredGPs, GazeDataFuture rawGazeDataFuture, GazeDataFuture filteredGazeDataFuture) {

		if (screenButtonModeIsActivated) {
			// check if screenButton is clicked
			if (recentLocation.x >= screenButton.getLocation().x && recentLocation.x <= screenButton.getLocation().x + screenButton.getWidth() && recentLocation.y >= screenButton.getLocation().y && recentLocation.y <= screenButton.getLocation().y + screenButton.getHeight()) {

				selectionTriggerMechanism.setDisabled();
				
				// end recording time
				timeEnd = System.currentTimeMillis();
				timeTickTriggered = System.nanoTime();
				log.info(System.currentTimeMillis() + " - " + "Stopped recording time");

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

				doSelection(screenButton);
				log.info(System.currentTimeMillis() + " - " + "Performed visual click on selected ScreenButton");
				
				// set _validBeforeTrigger list and recentLocation point back to the values which are responsible for the TargetItem trigger
				this.recentLocation = recentLocationCopy;
				_validGPbeforeTrigger = _validGPbeforeTriggerCopy;

				log.info(System.currentTimeMillis() + " - " + "Start to take screenshots");
				takeScreenshot();
				log.info(System.currentTimeMillis() + " - " + "Taking screenshots finished");

				// unmark last trigger targetItem
				selectTriggeredTargetItem(_targetItems.get(markedTargetItem), false);
				markedTargetItem = -1;

				// unmark target item
				_targetItems.get(intendedTargetItemIndex).setBackground(targetItemColor);

				// compute time needed to trigger button
				timeDuration = timeEnd - timeStart;

				log.info(System.currentTimeMillis() + " - " + "Start writing data to the XML element of current TestPlanItem");
				super.recordData(intendedTargetItemIndex,selectedTargetItemIndex,timeDuration,rawGPs,filteredGPs,this.rawGazeDataFuture,this.filteredGazeDataFuture);
				log.info(System.currentTimeMillis() + " - " + "Finished writing data to the XML element of current TestPlanItem");

				targetIndexCounter++;

				screenButtonModeIsActivated = false;

				// notify BasicGazeTargetOwner (TestManager), that the user performed the click and recording data is finished
				// because now, the owner can select the next intended target item
				// send recorded data to the GazeTarget owner too
				gazeTargetOwner.recordingDataInGazeTargetFinished(recordedData);				
			} else {
				// screenButtonMode is activated but screenButton was not clicked -> check if another targetItem was clicked
				checkIfTargetItemTriggered();
			}		
		} else {
			// screenButtonMode is NOT activated -> check if a tagretItem was clicked
			checkIfTargetItemTriggered();
		}
	}
	
	/**
	 * This method checks if a TargetItem was triggered. If this is the case, this TargetItems is shown as selected.
	 */
	@SuppressWarnings("unchecked")
	private void checkIfTargetItemTriggered() {
		Iterator<JButton> iter = _targetItems.iterator();
		for (int i=0; iter.hasNext(); i++) {
			JButton current = iter.next();
			if (recentLocation.x >= current.getLocation().x+targetItemsPanel.getLocation().x && recentLocation.x <= current.getLocation().x+targetItemsPanel.getLocation().x + current.getWidth() && recentLocation.y >= current.getLocation().y+targetItemsPanel.getLocation().y && recentLocation.y <= current.getLocation().y+targetItemsPanel.getLocation().y + current.getHeight()) {
				selectedTargetItemIndex = i;
				log.info(System.currentTimeMillis() + " - " + "Found selected TargetItem");
				// unmark last trigger targetItem
				if (markedTargetItem != -1) {
					selectTriggeredTargetItem(_targetItems.get(markedTargetItem), false);
				}
				// mark new trigger targetItem
				selectTriggeredTargetItem(_targetItems.get(i), true);
				log.info(System.currentTimeMillis() + " - " + "Set selected TargetItem as selected");
				markedTargetItem = i;
				// make a copy of the _validGPbeforeTrigger list and the recentLocation point
				recentLocationCopy = (Point) recentLocation.clone();
				_validGPbeforeTriggerCopy = (LinkedList<GazePoint>) _validGPbeforeTrigger.clone();	
				// activate screenButton mode
				screenButtonModeIsActivated = true;	
				// clear recorded future gaze data up to now
				if (storeFutureGazeData) {			
					if (rawGazeDataFuture != null) {
						rawGazeDataFuture.setIsListening(true);
					}
					if (filteredGazeDataFuture != null) {
						filteredGazeDataFuture.setIsListening(true);
					}	
					log.info(System.currentTimeMillis() + " - " + "Cleared recorded future gaze data up to now");
				}
				break;
			}
		}		
		// activate the selectionTriggerMechanism again
		selectionTriggerMechanism.setActivated();
		log.info(System.currentTimeMillis() + " - " + "Activated the SelectionTriggerMechanism again");
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
	
	/*
	 * (non-Javadoc)
	 * @see gaze_selection_library.BasicGazeTarget#highlightTarget(gaze_library.GazePoint)
	 */
	@Override
	protected void highlightTarget(GazePoint gazePoint) {
		// check if the given gazepoint locaction fits to one of the targetItems -> highlighting
		Iterator<JButton> iter = _targetItems.iterator();
		int currentTargetIndex = -1;
		for (int i=0; iter.hasNext(); i++) {
			JButton current = iter.next();
			if (gazePoint.getLocation().x >= current.getLocation().x+targetItemsPanel.getLocation().x && gazePoint.getLocation().x <= current.getLocation().x+targetItemsPanel.getLocation().x + current.getWidth() && gazePoint.getLocation().y >= current.getLocation().y+targetItemsPanel.getLocation().y && gazePoint.getLocation().y <= current.getLocation().y+targetItemsPanel.getLocation().y + current.getHeight()) {
				currentTargetIndex = i;
				break;
			}
		}
		// check if gazepoint location fits to screenbutton
		if (gazePoint.getLocation().x >= screenButton.getLocation().x && gazePoint.getLocation().x <= screenButton.getLocation().x + screenButton.getWidth() && gazePoint.getLocation().y >= screenButton.getLocation().y && gazePoint.getLocation().y <= screenButton.getLocation().y + screenButton.getHeight()) {
			currentTargetIndex = _targetItems.size();
		}				
			
		// highlight target as feedback
		if (lastTargetIndex != -1) {
			if (lastTargetIndex != currentTargetIndex) {
				if (lastTargetIndex == _targetItems.size()) {
					screenButton.setSelected(false);
				} else {
					_targetItems.get(lastTargetIndex).setSelected(false);
				}
			}
		}
		if (currentTargetIndex != -1) {
			if (lastTargetIndex != currentTargetIndex) {
				if (currentTargetIndex == _targetItems.size()) {
					screenButton.setSelected(true);
				} else {
					_targetItems.get(currentTargetIndex).setSelected(true);
				}
			}
		}
		lastTargetIndex = currentTargetIndex;
	}
	
	/*
	 * (non-Javadoc)
	 * @see gaze_selection_library.BasicGazeTarget#triggerEventHappend(gaze_selection_library.TriggerEvent)
	 */
	@Override
	public void triggered(TriggerEvent triggerEvent) {
		if (storeFutureGazeData && !screenButtonModeIsActivated) {			
			if (rawGazeDataFuture != null) {
				rawGazeDataFuture.setIsListening(true);
			}
			if (filteredGazeDataFuture != null) {
				filteredGazeDataFuture.setIsListening(true);
			}	
			log.info(System.currentTimeMillis() + " - " + "Started recording future gaze data");
		}
		synchronized (triggerThread) {
			triggerThread.notify();
		}		
	}

}
