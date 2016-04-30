package gaze_selection_library;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

import gaze_library.*;

/**
 * @author Thorsten Bövers
 * This class implements a SelectionTriggerMechanism which reacts to winking of one eyelid and works as follows:
 * If the registered GazeDataProvider sends a PARTIALLY VALID GazePoint, then a timer is started with a given end time.
 * If the following GazePoints are also PARTIALLY VALID until the end time is reached, the trigger event is generated and
 * all observing SelectionTriggerMechanismListener are notified.
 * Otherwise if a INVALID or VALID GazePoint is received within this time, the timer is stopped and the task removed.
 */
public class WinkTriggerMechanism extends SelectionTriggerMechanism implements GazePointListener {

	/**
	 * Stores the time in milliseconds within all GazePoints have to be PARTIALLY VALID.
	 */
	private int winkTimeInMilliseconds;

	/**
	 * Flag is set if the timer is already started.
	 */
	private boolean timerStarted = false;

	/**
	 * Timer object which stores the TimerTask.
	 */
	private Timer timer = new Timer();

	/**
	 * TimerTask object which task is to notify all observing SelectionTriggerMechanismListener
	 */
	private TimerTask timerTask;

	/**
	 * This variable stores a reference to the GazeDataProvider.
	 */
	public GazeDataProvider gazeDataProvider;

	/**
	 * Thread which is responsible for the timer.
	 * Therefore another thread is used to speed up the system.
	 */
	private Thread winkThread;

	/**
	 * Queue which store new GazePoints.
	 */
	private ConcurrentLinkedQueue<GazePoint> concurrentGPQueue = new ConcurrentLinkedQueue<GazePoint>();

	/**
	 * Standard constructor
	 * @param gazeDataProvider
	 * @param winkTimeInMilliseconds
	 */
	public WinkTriggerMechanism(GazeDataProvider gazeDataProvider, int winkTimeInMilliseconds) {		
		this.winkTimeInMilliseconds = winkTimeInMilliseconds;
		this.gazeDataProvider = gazeDataProvider;

		// create a new thread which is responsible for the variance computation
		winkThread = new Thread() {
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
							checkGPWink(concurrentGPQueue.remove());
						}
					}
				}
			}
		};
		winkThread.start();

	}

	/* (non-Javadoc)
	 * @see gaze_library.GazePointListener#gazePointHappened(gaze_library.GazePointEvent)
	 */
	@Override
	public void gazePointHappened(GazePointEvent gazePointEvent) {
		concurrentGPQueue.add(gazePointEvent.getGazePoint());
		synchronized (winkThread) {
			winkThread.notify();
		}
	}

	/**
	 * This method checks if a given GazePoint is PARTIALLY_VALID and starts if necessary the wink timer or stops it.
	 * @param gazePoint
	 */
	private void checkGPWink(GazePoint gazePoint) {
		// only if the SelectionTriggerMechanism is activated incoming GazePointEvents are relevant
		// this is to avoid unwanted trigger events e.g. when the GazeTarget is not already started
		if (isActivated) {
			if (timerStarted) {
				// case when the timer is already started, but the following GazePoint is not PARTIALLY VALID -> cancel TimerTask
				if (!gazePoint.getValidity().equals(Validity.PARTIALLY_VALID)) {
					timerTask.cancel();
					// remove all canceled TimerTask from the timer schedule
					timer.purge();
					timerStarted = false;

				} else {
					// case when the timer is already started and the following GazePoint is again PARTIALLY VALID -> do not stop timer
				}
			} else {
				// case when the timer is not started and the current GazePoint is PARTIALLY VALID
				// -> add new TimerTask to the timer with the given winkTime
				if (gazePoint.getValidity().equals(Validity.PARTIALLY_VALID)) {
					timerTask = new Task();
					timer.schedule(timerTask, winkTimeInMilliseconds);
					timerStarted = true;
				}
			}
		}
	}

	/**
	 * @author Thorsten Bövers
	 * This TimerTask implements the run-method which sets the time expired flag.
	 * This means that the timerTask's end time is expired and all observers can be notified.
	 */
	class Task extends TimerTask {
		/* (non-Javadoc)
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {
			if (isActivated) {
				log.info(System.currentTimeMillis() + " - " + "TriggerEvent generated: Wink time expired");
				notifyAllSelectionTriggerMechanismListener();
			}
			// remove this mechanism from the GazeDataProvider
			gazeDataProvider.removeGazePointListener(WinkTriggerMechanism.this);
			timerStarted = false;
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
		// remove this mechanism from the GazeDataProvider
		gazeDataProvider.removeGazePointListener(WinkTriggerMechanism.this);
		timerTask.cancel();
		// remove all canceled TimerTask from the timer schedule
		timer.purge();
		timerStarted = false;
		
	}

}
