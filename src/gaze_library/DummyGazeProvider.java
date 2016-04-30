package gaze_library;

import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

/**
 * @author Thorsten Bövers
 * This class is used to simulate gaze point data using the mouse cursor.
 * It creates with a given frequency simulated GazePoints using the mouse.
 * After generating a GazePoint object all observing GazePointListener are notified.
 * To simulate winking, GazePoints are PARTIALLY_VALID if the mouse button is pressed.
 */
public class DummyGazeProvider implements GazeDataProvider, MouseListener {
	
	/**
	 * This linkedList stores all GazePointListener, which are notified if a GazePointEvent happened.
	 */
	private LinkedList<GazePointListener> _gpListeners = new LinkedList<GazePointListener>();

	/**
	 * This variable stores the tracking frequency of DummyGazeDataProvider and is set in the the constructor.
	 */
	private int trackerFrequency;

	/**
	 * This thread is responsible for listening on the mouse cursor position. 
	 */
	private Thread listeningThread = null;

	/**
	 * If this flag is set, the listeningThread stops listening.
	 */
	private boolean stopListeningThread = false;

	/**
	 * Thread which only notifies observers if new DummyGazePoints were created.
	 * Is used to speed up the system.
	 */
	private Thread notifyThread;

	/**
	 * Queue which stores new DummyGazePoints.
	 */
	private ConcurrentLinkedQueue<GazePoint> concurrentGPQueue = new ConcurrentLinkedQueue<GazePoint>();

	/**
	 * Flag is set if mouse button is pressed, and not set if mouse button is not pressed.
	 */
	private boolean isMousedClicked = false;

	/**
	 * Standard constructor which creates a new thread for creating GazePoints with a given frequency.
	 * It's important to call the close method before loosing the reference.
	 * @param freq With this frequency dummy GazePoints are created
	 */
	public DummyGazeProvider(int freq) {
		trackerFrequency = freq;

		// create a new Thread for notifying observers if new GazePoints were generated
		notifyThread = new Thread() {
			@Override
			public void run() {
				while (!stopListeningThread) {
					synchronized (this) {
						try {
							wait();
						} catch (InterruptedException e) {
							log.log(Level.SEVERE,null, e);
						}
						while (!concurrentGPQueue.isEmpty()) {
							fireGPEvent(concurrentGPQueue.remove());
						}
					}
				}
			}
		};
		notifyThread.start();

		// create a new thread for creating dummy GazePoints with the given frequency
		listeningThread = new Thread() {
			@Override
			public void run() {
				while (!stopListeningThread) {
					int x = (int) MouseInfo.getPointerInfo().getLocation().getX();
					int y = (int) MouseInfo.getPointerInfo().getLocation().getY();
					long time = System.nanoTime();

					SimpleGazePoint gp;
					// if mouse button is clicked, GazePoint is PARTIALLY_VALID to simulate winking
					if (isMousedClicked) {
						gp = new SimpleGazePoint(x, y, 1, time);
					} else {
						gp = new SimpleGazePoint(x, y, 2, time);
					}

					// add new GazePoint to Queue
					concurrentGPQueue.add(gp);

					if (notifyThread != null) {
						synchronized (notifyThread) {
							notifyThread.notify();
						}
					}

					// sleep thread to implement tracking frequency
					try {
						Thread.sleep(1000/trackerFrequency);
					} catch (InterruptedException e) {
						log.log(Level.SEVERE,null, e);
					}
				}		
			}
		};
		listeningThread.start();
	}

	/*
	 * (non-Javadoc)
	 * @see gaze_library.GazeDataProvider#closeGazeDataProvider()
	 */
	@Override
	public synchronized void closeGazeDataProvider() throws IOException {
		stopListeningThread = true;
		listeningThread = null;
		synchronized (notifyThread) {
			notifyThread = null;
		}
	}

	/* (non-Javadoc)
	 * @see gaze_library.GazeDataProvider#addGazePointListener(gaze_library.GazePointListener)
	 */
	@Override
	public synchronized void addGazePointListener(GazePointListener listener) {
		_gpListeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see gaze_library.GazeDataProvider#removeGazePointListener(gaze_library.GazePointListener)
	 */
	@Override
	public synchronized void removeGazePointListener(GazePointListener listener) {
		_gpListeners.remove(listener);
	}

	/**
	 * This method creates for a given GazePoint a GazePointEvent and notifies all registered GazePointListeners
	 * @param gazePoint GazePoint object
	 */
	private synchronized void fireGPEvent(GazePoint gazePoint) {
		GazePointEvent gazePointEvent = new GazePointEvent(this);
		// bind gazePoint to specific gazePointEvent
		gazePointEvent.setGazePoint(gazePoint);		
		for (GazePointListener gplistener : _gpListeners){
			gplistener.gazePointHappened(gazePointEvent);
		}
	}

	/* (non-Javadoc)
	 * @see gaze_library.GazeDataProvider#getTrackerFrequency()
	 */
	@Override
	public int getTrackerFrequency() {
		return trackerFrequency;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		isMousedClicked = true;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		isMousedClicked = false;		
	}

}

