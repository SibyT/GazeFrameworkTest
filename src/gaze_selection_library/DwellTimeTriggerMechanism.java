package gaze_selection_library;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

import gaze_library.*;

/**
 * @author Thorsten Bövers
 * This class implements a SelectionTriggerMechanism which generates a trigger event if the location of different GazePoints
 * are geometric closed to each other for a given time, the dwell time.
 * This implementation works as follows:
 * Objects of this class are registered at a GazeDataProvider to receive GazePoints. These GazePoints are stored in a
 * LinkedList with the length of the given dwell time.
 * If the this list is full-filled  for the first time, the variance of all values up to this time is computed.
 * When in the next times a new GazePoint is received, only the difference to the last GazePoint is computed and the
 * value of the variance is updated.
 * If the variance is under a given threshold, the accuracy, then all observers are notified with a trigger event.
 * The description of the algorithm for computing the variance is described in my thesis.
 */
public class DwellTimeTriggerMechanism extends SelectionTriggerMechanism implements GazePointListener {

	/**
	 * This LinkedList stores all receiving GazePoints for the given dwell time
	 */
	private LinkedList<GazePoint> _gazePoints = new LinkedList<GazePoint>();

	/**
	 * Parameter for the dwell time in milliseconds
	 */
	private int dwellTimeDurationMilliseconds;

	/**
	 * Parameter for the specific GazeDataProvider frequency
	 */
	private int trackerFrequency;

	/**
	 * This value describes the required accuracy between the different GazePoints to trigger the selection
	 * In this implementation it is described by the variance of the valid GazePoints in the LinkedList.
	 */
	private int accuracy;

	/**
	 * This variable stores a reference to the GazeDataProvider.
	 */
	private GazeDataProvider gazeDataProvider;

	/**
	 * Thread which computes the variance.
	 * Therefore another thread is used to speed up the system.
	 */
	private Thread varianceComputationThread;

	/**
	 * Queue which store new GazePoints.
	 */
	private ConcurrentLinkedQueue<GazePoint> concurrentGPQueue = new ConcurrentLinkedQueue<GazePoint>();

	/**
	 * Stores the sum of all x-coordinate values in the _gazePoints list needed for the variance computation.
	 */
	private int sumX = 0;

	/**
	 * Stores the sum of all y-coordinate values in the _gazePoints list needed for the variance computation.
	 */
	private int sumY = 0;

	/**
	 * Stores the squared sum of all x-coordinate values in the _gazePoints list needed for the variance computation.
	 */
	private long sumSquaredX = 0;

	/**
	 * Stores the squared sum of all y-coordinate values in the _gazePoints list needed for the variance computation.
	 */
	private long sumSquaredY = 0;

	/**
	 * Stores the number of valid GazePoints in the _gazePoints list needed for the variance computation.
	 */
	private int numberOfValidGazePoints = 0;

	/**
	 * Flag to know whether the sums were already computed for the first time.
	 */
	private boolean sumAlreadyComputed = false;

	/**
	 * Stores the last GazePoint which was removed before the new GazePoint was inserted in the _gazePoints list.
	 */
	private GazePoint lastGazePoint;

	/**
	 * Standard constructor
	 * @param gazeDataProvider
	 * @param dwellTimeDurationMilliseconds
	 * @param trackerFrequency
	 * @param accuracy
	 */
	public DwellTimeTriggerMechanism(GazeDataProvider gazeDataProvider, int dwellTimeDurationMilliseconds, int trackerFrequency, int accuracy) {
		this.dwellTimeDurationMilliseconds = dwellTimeDurationMilliseconds;
		this.trackerFrequency = trackerFrequency;
		this.accuracy = accuracy;
		this.gazeDataProvider = gazeDataProvider;

		// create a new thread which is responsible for the variance computation
		varianceComputationThread = new Thread() {
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
							computeVariance(concurrentGPQueue.remove());
						}
					}
				}
			}
		};
		varianceComputationThread.start();
	}

	/* (non-Javadoc)
	 * @see gaze_library.GazePointListener#gazePointHappened(gaze_library.GazePointEvent)
	 */
	@Override
	public void gazePointHappened(GazePointEvent gazePointEvent) {
		// add new GazePoint to queue and notify the varianceComputationThread
		concurrentGPQueue.add(gazePointEvent.getGazePoint());
		synchronized (varianceComputationThread) {
			varianceComputationThread.notify();
		}
	}

	/**
	 * This function computes the variance depending on the new GazePoint
	 * @param gazePoint
	 */
	private void computeVariance(GazePoint gazePoint) {

		if (isActivated) {
			// stores GazePoints for the last given dwell time in milliseconds
			if (_gazePoints.size() < trackerFrequency*dwellTimeDurationMilliseconds/1000) {
				_gazePoints.addFirst(gazePoint);
			}
			else {
				lastGazePoint = _gazePoints.removeLast();
				_gazePoints.addFirst(gazePoint);
			}

			// got already enough gaze points -> compute variance
			if (_gazePoints.size() == trackerFrequency*dwellTimeDurationMilliseconds/1000) {

				// compute sum and sumSquared for the first time
				if (!sumAlreadyComputed) {
					Iterator<GazePoint> iter = _gazePoints.iterator();
					while (iter.hasNext()) {
						GazePoint current = iter.next();
						if (current.getValidity().equals(Validity.VALID)) {
							sumX += current.getLocation().x;
							sumY += current.getLocation().y;
							sumSquaredX += (current.getLocation().x * current.getLocation().x);
							sumSquaredY += (current.getLocation().y * current.getLocation().y);
							numberOfValidGazePoints++;
						}
					}
					sumAlreadyComputed = true;

					// sum and sumSquared were already computed last time -> only update sum and sumSquared with new value
				} else {
					if (lastGazePoint.getValidity().equals(Validity.VALID)) {
						sumX -= lastGazePoint.getLocation().x;
						sumY -= lastGazePoint.getLocation().y;
						sumSquaredX -= (lastGazePoint.getLocation().x * lastGazePoint.getLocation().x);
						sumSquaredY -= (lastGazePoint.getLocation().y * lastGazePoint.getLocation().y);
						numberOfValidGazePoints--;
					}
					if (gazePoint.getValidity().equals(Validity.VALID)) {
						sumX += gazePoint.getLocation().x;
						sumY += gazePoint.getLocation().y;
						sumSquaredX += (gazePoint.getLocation().x * gazePoint.getLocation().x);
						sumSquaredY += (gazePoint.getLocation().y * gazePoint.getLocation().y);
						numberOfValidGazePoints++;
					}
				}

				// compute the variance if at least 90 percent of the recorded gaze points were valid
				if (numberOfValidGazePoints>=_gazePoints.size()*0.9) {

					int meanValueX = sumX / numberOfValidGazePoints;
					int meanValueY = sumY / numberOfValidGazePoints;

					if (meanValueX!=0 && meanValueY!=0) {

						long varianceX = (sumSquaredX / numberOfValidGazePoints) - (meanValueX * meanValueX);
						long varianceY = (sumSquaredY / numberOfValidGazePoints) - (meanValueY * meanValueY);

						// check if calculated variance fits into the given accuracy
						if (varianceX < accuracy && varianceX > -accuracy && varianceY < accuracy && varianceY > -accuracy) {
							log.info(System.currentTimeMillis() + " - " + "TriggerEvent generated: Dwell time expired");
							if (isActivated) {
								notifyAllSelectionTriggerMechanismListener();
							}

							// remove this mechanism from the GazeDataProvider
							gazeDataProvider.removeGazePointListener(this);
							_gazePoints.clear();
							sumX = 0;
							sumY = 0;
							sumSquaredX = 0;
							sumSquaredY = 0;
							numberOfValidGazePoints = 0;
							sumAlreadyComputed = false;
						}
					}
				}
			}
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
		// remove this mechanism from the GazeDataProvider
		gazeDataProvider.removeGazePointListener(this);
		_gazePoints.clear();
		sumX = 0;
		sumY = 0;
		sumSquaredX = 0;
		sumSquaredY = 0;
		numberOfValidGazePoints = 0;
		sumAlreadyComputed = false;
		
	}

}
