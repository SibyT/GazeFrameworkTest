package gaze_selection_library;

import gaze_library.*;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

/**
 * @author Thorsten Bövers
 * This is a default implementation class of the GazeDataFuture interface.
 * Objects of this class run in a background thread to avoid blocking the main thread.
 * It is the responsibility of this objects to gather future gaze data from a specific GazeDataProvider.
 * In this implementation the location of all GazePoints is mapped to the given GazeTarget location.
 */
public class FutureGazeReader implements GazeDataFuture, GazePointListener {

	/**
	 * Specific GazeDataProvider to listen to.
	 */
	private GazeDataProvider gazeDataProvider = null;

	/**
	 * In this LinkedList all future GazePoints are stored until the isListen flag is not set.
	 */
	private LinkedList<GazePoint> _futureGazePoints = new LinkedList<GazePoint>();

	/**
	 * Flag to stop listening to the GazeDataProvider
	 */
	private boolean isListening = false;

	/**
	 * Reference to the owner of this object. Is needed to compute the GazePoint location depending on the GazeTarget location.
	 */
	private GazeTarget gazeTarget;
	
	/**
	 * Thread which is responsible for processing received GazePointEvents.
	 * Therefore another thread is used to speed up the system.
	 */
	private Thread futureGazeReaderThread;

	/**
	 * Queue which stores new GazePoints.
	 */
	private ConcurrentLinkedQueue<GazePoint> concurrentGPQueue = new ConcurrentLinkedQueue<GazePoint>();

	/**
	 * Standard constructor
	 * @param gazeDataProvider to listen to
	 * @param gazeTarget as object owner
	 */
	public FutureGazeReader(GazeDataProvider gazeDataProvider, GazeTarget gazeTarget) {
		this.gazeDataProvider = gazeDataProvider;
		this.gazeTarget = gazeTarget;
		
		// create a new thread which is responsible for the variance computation
		futureGazeReaderThread = new Thread() {
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
							processGazePoint(concurrentGPQueue.remove());
						}
					}
				}
			}
		};
		futureGazeReaderThread.start();
	}

	/* (non-Javadoc)
	 * @see gaze_selection_library.GazeDataFuture#getFutureGazePoint()
	 */
	@Override
	public LinkedList<GazePoint> getFutureGazePoints() {
		return _futureGazePoints;
	}

	/* (non-Javadoc)
	 * @see gaze_library.GazePointListener#gazePointHappened(gaze_library.GazePointEvent)
	 */
	@Override
	public void gazePointHappened(GazePointEvent gazePointEvent) {
		concurrentGPQueue.add(gazePointEvent.getGazePoint());
		synchronized (futureGazeReaderThread) {
			futureGazeReaderThread.notify();
		}
	}
		
	private void processGazePoint(GazePoint gazePoint) {
		if (isListening) {

			// create new GazePoint with updated location regarding to the given GazeTarget panel
			int validity;
			switch (gazePoint.getValidity()) {
			case PARTIALLY_VALID:
				validity = 1;
				break;
			case VALID:
				validity = 2;
				break;
			case INVALID:
				validity = 0;
				break;
			default:
				validity = 0;
				break;
			}
			GazePoint newGazePoint;
			if (gazePoint instanceof GP3GazePoint) {
				newGazePoint = new GP3GazePoint(gazePoint.getLocation().x-gazeTarget.getLocationOnScreen().x, gazePoint.getLocation().y-gazeTarget.getLocationOnScreen().y, validity, gazePoint.getTimestamp());
			} else if(gazePoint instanceof TheEyeTribeGazePoint){
				newGazePoint = new TheEyeTribeGazePoint(gazePoint.getLocation().x-gazeTarget.getLocationOnScreen().x, gazePoint.getLocation().y-gazeTarget.getLocationOnScreen().y, validity, gazePoint.getTimestamp());

		}else {
				newGazePoint = new SimpleGazePoint(gazePoint.getLocation().x-gazeTarget.getLocationOnScreen().x, gazePoint.getLocation().y-gazeTarget.getLocationOnScreen().y, validity, gazePoint.getTimestamp());
			}

			// add new GazePoint to the list
			_futureGazePoints.addLast(newGazePoint);
		}	
	}

	/*
	 * (non-Javadoc)
	 * @see gaze_selection_library.GazeDataFuture#setIsListening(boolean)
	 */
	@Override
	public void setIsListening(boolean value) {			
		if (value == true) {
			// clear list before starting to record future GazePoints
			_futureGazePoints.clear();
			// check whether FutureGazeReader is already registered at the GazeDataProvider
			if (!isListening) {
				// register at GazeDataProvider
				gazeDataProvider.addGazePointListener(this);
			}
		} else {
			// remove from GazeDataProvider
			gazeDataProvider.removeGazePointListener(this);
		}
		this.isListening = value;
	}

}
