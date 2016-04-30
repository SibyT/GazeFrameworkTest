package gaze_selection_library;

import gaze_library.GazeDataProvider;
import gaze_library.GazePoint;
import gaze_library.GazePointEvent;
import gaze_library.GazePointListener;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

/**
 * @author Thorsten Bövers
 * This class implements a SelectionTriggerMechanism which generates a trigger event if the a specified gesture was performed.
 * This implementation works as follows:
 * Objects of this class are registered at a GazeDataProvider to receive GazePoints.
 * The gesture is specified by GesturePoints on the screen. The received GazePoints have to be located in the order of the
 * GesturePoints in the gesture list. To accept some inaccuracy within the GesturePoints a circle with a given radius is specified.
 * All GazePoints within this circle are accepted for a gesture.
 * If a GazePoint is received the algorithm checks if the next GesturePoint of the gesture list fits into the mandatory GesturePoint.
 * If this is the case the index of the list is increased.
 * If all GesturePoints are processed a TriggerEvent is generated.
 * Every time when a GazePoint is received the algorithm checks if the last recognized GesturePoint is too old, the index of the Gesture list is reset.
 * This is to guarantee that the gesture is performed in a specified time.
 * On the screenshots the gesture is shown as future GazePoints.
 */
public class GestureTriggerMechanism extends SelectionTriggerMechanism implements GazePointListener {

	/**
	 * List which stores the order of GesturePoints. Only if a gesture in this order if performed a TriggerEvent is generated.
	 */
	private List<GesturePoint> gesture = Arrays.asList(GesturePoint.TOP_LEFT,GesturePoint.CENTER,GesturePoint.BOTTOM_RIGHT,GesturePoint.CENTER_RIGHT,GesturePoint.TOP_RIGHT,GesturePoint.CENTER,GesturePoint.BOTTOM_LEFT);
	
	/**
	 * Defines an accepted radius in pixels around a GesturePoint.
	 */
	private int radius;

	/**
	 * Gives an upper bound for the time between two recognized GesturePoints.
	 */
	private int timeBetweenGesturePointsInMilliseconds;

	/**
	 * Stores the index for the Gesture list which shows which GesturePoint is required next.
	 */
	private int gestureIndex = 0;

	/**
	 * Stores the timestamp in milliseconds when the last GesturePoint was recognized.
	 */
	private long lastGestureRecognitionTimeStamp = -1;

	/**
	 * This variable stores a reference to the GazeDataProvider.
	 */
	private GazeDataProvider gazeDataProvider;

	/**
	 * This variable stores a reference to the GazeTarget.
	 */
	private GazeTarget gazeTarget;

	/**
	 * Thread which recognizes gestures.
	 * Therefore another thread is used to speed up the system.
	 */
	private Thread gestureRecognitionThread;

	/**
	 * Queue which stores new GazePoints.
	 */
	private ConcurrentLinkedQueue<GazePoint> concurrentGPQueue = new ConcurrentLinkedQueue<GazePoint>();

	/**
	 * Standard constructor which reads in the properties and starts a new thread for gesture recognition.
	 * @param gazeDataProvider
	 * @param gazeTarget
	 * @param radius defines an accepted circle in pixels around a GesturePoint
	 * @param timeBetweenGesturePointsInMilliseconds is an upper bound for the time between two recognized GesturePoints
	 */
	public GestureTriggerMechanism(GazeDataProvider gazeDataProvider, GazeTarget gazeTarget, int radius, int timeBetweenGesturePointsInMilliseconds) {
		this.gazeDataProvider = gazeDataProvider;
		this.gazeTarget = gazeTarget;
		this.radius = radius;
		this.timeBetweenGesturePointsInMilliseconds = timeBetweenGesturePointsInMilliseconds;

		// create a new thread which is responsible for the gesture recognition
		gestureRecognitionThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					synchronized (this) {
						try {
							wait();
						} catch (InterruptedException e) {
							log.log(Level.SEVERE,null, e);
						}
						while (!concurrentGPQueue.isEmpty()) {
							recognizeGesture(concurrentGPQueue.remove());
						}
					}
				}
			}
		};
		gestureRecognitionThread.start();
	}

	/**
	 * This function recognizes a gesture depending on the new GazePoint
	 * @param gazePoint
	 */
	private void recognizeGesture(GazePoint gazePoint) {
		if (isActivated) {
			// reset index of the gesture state if last gesture recognition is too old
			if (System.currentTimeMillis()-lastGestureRecognitionTimeStamp >= timeBetweenGesturePointsInMilliseconds) {
				gestureIndex = 0;
			}

			// check if the GazePoint position correlates to the next expected gesture position
			switch (gesture.get(gestureIndex)) {
			case BOTTOM_LEFT:
				if (gazePoint.getLocation().x >= gazeTarget.getLocationOnScreen().x-radius && gazePoint.getLocation().x <= gazeTarget.getLocationOnScreen().x+radius
				&& gazePoint.getLocation().y >= gazeTarget.getLocationOnScreen().y+gazeTarget.getSize().height-radius && gazePoint.getLocation().y <= gazeTarget.getLocationOnScreen().y+gazeTarget.getSize().height+radius) {
					log.info(System.currentTimeMillis() + " - " + "GesturePoint recognized: BOTTOM_LEFT");
					lastGestureRecognitionTimeStamp = System.currentTimeMillis();
					gestureIndex++;
				}
				break;
			case BOTTOM_RIGHT:
				if (gazePoint.getLocation().x >= gazeTarget.getLocationOnScreen().x+gazeTarget.getSize().width-radius && gazePoint.getLocation().x <= gazeTarget.getLocationOnScreen().x+gazeTarget.getSize().width+radius
				&& gazePoint.getLocation().y >= gazeTarget.getLocationOnScreen().y+gazeTarget.getSize().height-radius && gazePoint.getLocation().y <= gazeTarget.getLocationOnScreen().y+gazeTarget.getSize().height+radius) {
					log.info(System.currentTimeMillis() + " - " + "GesturePoint recognized: BOTTOM_RIGHT");
					lastGestureRecognitionTimeStamp = System.currentTimeMillis();
					gestureIndex++;
				}
				break;
			case CENTER:
				if (gazePoint.getLocation().x >= gazeTarget.getLocationOnScreen().x+(gazeTarget.getSize().width/2)-radius && gazePoint.getLocation().x <= gazeTarget.getLocationOnScreen().x+(gazeTarget.getSize().width/2)+radius
				&& gazePoint.getLocation().y >= gazeTarget.getLocationOnScreen().y+(gazeTarget.getSize().height/2)-radius && gazePoint.getLocation().y <= gazeTarget.getLocationOnScreen().y+(gazeTarget.getSize().height/2)+radius) {
					log.info(System.currentTimeMillis() + " - " + "GesturePoint recognized: CENTER");
					lastGestureRecognitionTimeStamp = System.currentTimeMillis();
					gestureIndex++;
				}
				break;
			case CENTER_BOTTOM:
				if (gazePoint.getLocation().x >= gazeTarget.getLocationOnScreen().x+(gazeTarget.getSize().width/2)-radius && gazePoint.getLocation().x <= gazeTarget.getLocationOnScreen().x+(gazeTarget.getSize().width/2)+radius
				&& gazePoint.getLocation().y >= gazeTarget.getLocationOnScreen().y+gazeTarget.getSize().height-radius && gazePoint.getLocation().y <= gazeTarget.getLocationOnScreen().y+gazeTarget.getSize().height+radius) {
					log.info(System.currentTimeMillis() + " - " + "GesturePoint recognized: CENTER_BOTTOM");
					lastGestureRecognitionTimeStamp = System.currentTimeMillis();
					gestureIndex++;
				}
				break;
			case CENTER_LEFT:
				if (gazePoint.getLocation().x >= gazeTarget.getLocationOnScreen().x-radius && gazePoint.getLocation().x <= gazeTarget.getLocationOnScreen().x+radius
				&& gazePoint.getLocation().y >= gazeTarget.getLocationOnScreen().y+(gazeTarget.getSize().height/2)-radius && gazePoint.getLocation().y <= gazeTarget.getLocationOnScreen().y+(gazeTarget.getSize().height/2)+radius) {
					log.info(System.currentTimeMillis() + " - " + "GesturePoint recognized: CENTER_LEFT");
					lastGestureRecognitionTimeStamp = System.currentTimeMillis();
					gestureIndex++;
				}
				break;
			case CENTER_RIGHT:
				if (gazePoint.getLocation().x >= gazeTarget.getLocationOnScreen().x+gazeTarget.getSize().width-radius && gazePoint.getLocation().x <= gazeTarget.getLocationOnScreen().x+gazeTarget.getSize().width+radius
				&& gazePoint.getLocation().y >= gazeTarget.getLocationOnScreen().y+(gazeTarget.getSize().height/2)-radius && gazePoint.getLocation().y <= gazeTarget.getLocationOnScreen().y+(gazeTarget.getSize().height/2)+radius) {
					log.info(System.currentTimeMillis() + " - " + "GesturePoint recognized: CENTER_RIGHT");
					lastGestureRecognitionTimeStamp = System.currentTimeMillis();
					gestureIndex++;
				}
				break;
			case CENTER_TOP:
				if (gazePoint.getLocation().x >= gazeTarget.getLocationOnScreen().x+(gazeTarget.getSize().width/2)-radius && gazePoint.getLocation().x <= gazeTarget.getLocationOnScreen().x+(gazeTarget.getSize().width/2)+radius
				&& gazePoint.getLocation().y >= gazeTarget.getLocationOnScreen().y-radius && gazePoint.getLocation().y <= gazeTarget.getLocationOnScreen().y+radius) {
					log.info(System.currentTimeMillis() + " - " + "GesturePoint recognized: CENTER_TOP");
					lastGestureRecognitionTimeStamp = System.currentTimeMillis();
					gestureIndex++;
				}
				break;
			case TOP_LEFT:
				if (gazePoint.getLocation().x >= gazeTarget.getLocationOnScreen().x-radius && gazePoint.getLocation().x <= gazeTarget.getLocationOnScreen().x+radius
				&& gazePoint.getLocation().y >= gazeTarget.getLocationOnScreen().y-radius && gazePoint.getLocation().y <= gazeTarget.getLocationOnScreen().y+radius) {
					log.info(System.currentTimeMillis() + " - " + "GesturePoint recognized: TOP_LEFT");
					lastGestureRecognitionTimeStamp = System.currentTimeMillis();
					gestureIndex++;
				}
				break;
			case TOP_RIGHT:
				if (gazePoint.getLocation().x >= gazeTarget.getLocationOnScreen().x+gazeTarget.getSize().width-radius && gazePoint.getLocation().x <= gazeTarget.getLocationOnScreen().x+gazeTarget.getSize().width+radius
				&& gazePoint.getLocation().y >= gazeTarget.getLocationOnScreen().y-radius && gazePoint.getLocation().y <= gazeTarget.getLocationOnScreen().y+radius) {
					log.info(System.currentTimeMillis() + " - " + "GesturePoint recognized: TOP_RIGHT");
					lastGestureRecognitionTimeStamp = System.currentTimeMillis();
					gestureIndex++;
				}
				break;
			default:
				log.info(System.currentTimeMillis() + " - " + "Error in GestureTriggerMechanism: Unexpected state");
				break;

			}

			// check if gesture is done
			if (gestureIndex == gesture.size()) {
				// reset ordering index
				gestureIndex = 0;

				// remove this mechanism from the GazeDataProvider
				gazeDataProvider.removeGazePointListener(this);

				log.info(System.currentTimeMillis() + " - " + "TriggerEvent generated: Gesture performed");
				if (isActivated) {
					notifyAllSelectionTriggerMechanismListener();
				}
			}
		}
	}	

	/* (non-Javadoc)
	 * @see gaze_library.GazePointListener#gazePointHappened(gaze_library.GazePointEvent)
	 */
	@Override
	public void gazePointHappened(GazePointEvent gazePointEvent) {
		// add new GazePoint to queue and notify the gestureRecognitionThread
		concurrentGPQueue.add(gazePointEvent.getGazePoint());
		synchronized (gestureRecognitionThread) {
			gestureRecognitionThread.notify();
		}

	}

	/*
	 * (non-Javadoc)
	 * @see gaze_selection_library.SelectionTriggerMechanism#setActivated()
	 */
	@Override
	public synchronized void setActivated() {
		// register this mechanism at as GazePointListener at the GazeDataProvider		
		this.gazeDataProvider.addGazePointListener(this);

		isActivated = true;
	}

	/* (non-Javadoc)
	 * @see gaze_selection_library.SelectionTriggerMechanism#setDisabled()
	 */
	@Override
	public synchronized void setDisabled() {
		isActivated = false;
		// reset ordering index
		gestureIndex = 0;

		// remove this mechanism from the GazeDataProvider
		gazeDataProvider.removeGazePointListener(this);		
	}

}
