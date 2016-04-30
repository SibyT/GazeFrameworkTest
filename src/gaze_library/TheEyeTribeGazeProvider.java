package gaze_library;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

/**
 * @author Thorsten Bövers
 * This class is used for connecting and communicating with the GP3. It builds up a connection and listens on the port.
 * All observing GazePointListener are notified if new GazePoint objects are generated.
 */
public class TheEyeTribeGazeProvider implements GazeDataProvider {

	/**
	 * This linkedList stores all GazePointListener, which are notified if a GazePointEvent happened.
	 */
	private LinkedList<GazePointListener> _gpListeners = new LinkedList<GazePointListener>();

	/**
	 * This variable stores the tracking frequency of the GP3 eye tracker. The initial value is 60 Hz.
	 */
	private int trackerFrequency = 60;

	/**
	 * This thread is responsible for listening on the given port. If new data is received from the GP3,
	 * the incoming data will be processed.
	 */
	private Thread listeningThread = null;

	/**
	 * If this flag is set, the listeningThread stops listening.
	 */
	private boolean stopListeningThread = false;

	/**
	 * Thread which only notifies observers if new GP3GazePoints were created.
	 * Is used to speed up the system.
	 */
	private Thread notifyThread;

	/**
	 * Queue which stores new GP3GazePoints.
	 */
	private ConcurrentLinkedQueue<GazePoint> concurrentGPQueue = new ConcurrentLinkedQueue<GazePoint>();

	/**
	 * Socket for the connection to the GP3.
	 */
	private Socket tetClientSocket = null;

	/**
	 * Stores incoming data from the GP3.
	 * This variable needs to be final to have also access to it within the listeningThread.
	 */
	final private BufferedReader inFromServer;

	/**
	 * Stream for sending data to the GP3.
	 */
	private DataOutputStream outToServer = null;

	/**
	 * Standard constructor automatically connects to GP3 and listens on the given port in a new thread.
	 * It's important to call close method before loosing the reference.
	 * @param serverAddress for connecting to the GP3 eye tracker
	 * @param serverPort for connecting to the GP3 eye tracker
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public TheEyeTribeGazeProvider(String serverAddress, int serverPort) throws UnknownHostException, IOException {

		// open new socket and try to connect to the GP3 on the given ip address and port
		tetClientSocket = new Socket(serverAddress, serverPort);
System.out.println("teti  "+tetClientSocket.getInputStream());
System.out.println("teto  "+tetClientSocket.getOutputStream());

		// initialize in- and output buffer
		outToServer = new DataOutputStream(tetClientSocket.getOutputStream());
		inFromServer = new BufferedReader(new InputStreamReader(tetClientSocket.getInputStream()));

		// try to get the screen size from the GP3 to calculate the correct screen coordinates
		outToServer.writeBytes("<GET ID=\"SCREEN_SIZE\" />\r\n");

		// try to get the location of gazing (filtered with the internal fixation filter from the GP3)
		// the returned POGV is also true, if one eye is gathered, but this doesn't fit into this framework (valid, partially valid, invalid)
		// this means, that only the location is used from the POG values
		outToServer.writeBytes("<SET ID=\"ENABLE_SEND_POG_FIX\" STATE=\"1\" />\r\n");

		// try to get the high precision time tick
		outToServer.writeBytes("<SET ID=\"ENABLE_SEND_TIME_TICK\" STATE=\"1\" />\r\n");

		// try to get the left and right eye point of gazing, only important for the validity not for the location
		outToServer.writeBytes("<SET ID=\"ENABLE_SEND_POG_LEFT\" STATE=\"1\" />\r\n");
		outToServer.writeBytes("<SET ID=\"ENABLE_SEND_POG_RIGHT\" STATE=\"1\" />\r\n");

		// the GP3 sends the requested data with 60 Hz
		outToServer.writeBytes("<SET ID=\"ENABLE_SEND_DATA\" STATE=\"1\" />\r\n");

		// the received data looks like:
		// <REC TIME_TICK="318745821772" FPOGX="0.59384" FPOGY="0.15714" FPOGS="877.51111" FPOGD="0.09760" FPOGID="1627" FPOGV="1" LPOGX="0.61775" LPOGY="0.01911" LPOGV="1" RPOGX="0.53620" RPOGY="0.12895" RPOGV="1" />

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

		// create new thread for listening on port of GP3
		listeningThread = new Thread() {
			@Override
			public void run() {

				int screen_width = 0, screen_height = 0;
				long timetick = 0;
				float pogx = 0;
				float pogy = 0;
				int pogv = 0;
				int lpogv = 0;
				int rpogv = 0;

				String incomingData = "";
				int startIndex, endIndex;

				// while the flag isn't set, listen on the port and generate GazePoints
				while (!stopListeningThread) {
					try {
						incomingData = inFromServer.readLine();
					} catch (IOException e) {
						log.log(Level.SEVERE,"Catched InterruptedException", e);
					}

					// if GP3 sends screen size
					if (incomingData.startsWith("<ACK ID=\"SCREEN_SIZE\"")) {

						startIndex = incomingData.indexOf("WIDTH=\"") + "WIDTH=\"".length();
						endIndex = incomingData.indexOf("\"", startIndex);
						screen_width = Integer.parseInt(incomingData.substring(startIndex, endIndex));

						startIndex = incomingData.indexOf("HEIGHT=\"") + "HEIGHT=\"".length();
						endIndex = incomingData.indexOf("\"", startIndex);
						screen_height = Integer.parseInt(incomingData.substring(startIndex, endIndex));
					}

					// if GP3 sends gaze point data
					if (incomingData.startsWith("<REC TIME_TICK=")) {

						startIndex = incomingData.indexOf("TIME_TICK=\"") + "TIME_TICK=\"".length();
						endIndex = incomingData.indexOf("\"", startIndex);
						timetick = Long.parseLong(incomingData.substring(startIndex, endIndex));

						startIndex = incomingData.indexOf("FPOGX=\"") + "FPOGX=\"".length();
						endIndex = incomingData.indexOf("\"", startIndex);
						pogx = Float.parseFloat(incomingData.substring(startIndex, endIndex));

						startIndex = incomingData.indexOf("FPOGY=\"") + "FPOGY=\"".length();
						endIndex = incomingData.indexOf("\"", startIndex);
						pogy = Float.parseFloat(incomingData.substring(startIndex, endIndex));

						startIndex = incomingData.indexOf("LPOGV=\"") + "LPOGV=\"".length();
						endIndex = incomingData.indexOf("\"", startIndex);
						lpogv = Integer.parseInt(incomingData.substring(startIndex, endIndex));

						startIndex = incomingData.indexOf("RPOGV=\"") + "RPOGV=\"".length();
						endIndex = incomingData.indexOf("\"", startIndex);
						rpogv = Integer.parseInt(incomingData.substring(startIndex, endIndex));

						if (rpogv==1 && lpogv==1) {
							pogv = 2;
						} else if (rpogv==1 || lpogv==1) {
							pogv = 1;
						} else {
							pogv = 0;
						}

						// create for every incoming gaze point a GazePoint object after mapping data to the correct screen coordinates.
						TheEyeTribeGazePoint tet = new TheEyeTribeGazePoint((int)(pogx*screen_width), (int)(pogy*screen_height), pogv, timetick);

						// add new GazePoint to Queue
						concurrentGPQueue.add(tet);

						if (notifyThread != null) {
							synchronized (notifyThread) {
								notifyThread.notify();
							}
						}
					}
				}	
			}
		};
		listeningThread.start();
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see gaze_library.GazeDataProvider#closeGazeDataProvider()
	 */
	public synchronized void closeGazeDataProvider() throws IOException {
		// Stop the GP3 for sending data and closes all open sockets and buffers
		// Also interrupt the current listening thread
		outToServer.writeBytes("<SET ID=\"ENABLE_SEND_POG_FIX\" STATE=\"0\" />\r\n");
		outToServer.writeBytes("<SET ID=\"ENABLE_SEND_TIME_TICK\" STATE=\"0\" />\r\n");
		outToServer.writeBytes("<SET ID=\"ENABLE_SEND_POG_LEFT\" STATE=\"0\" />\r\n");
		outToServer.writeBytes("<SET ID=\"ENABLE_SEND_POG_RIGHT\" STATE=\"0\" />\r\n");
		outToServer.writeBytes("<SET ID=\"ENABLE_SEND_DATA\" STATE=\"0\" />\r\n");

		stopListeningThread = true;
		listeningThread = null;
		synchronized (notifyThread) {
			notifyThread = null;
		}
		inFromServer.close();
		outToServer.close();
		tetClientSocket.close();
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
		GazePointEvent gpEvent = new GazePointEvent(this);
		// bind GazePoint to specific GazePointEvent
		gpEvent.setGazePoint(gazePoint);
		for (GazePointListener gplistener : _gpListeners){
			gplistener.gazePointHappened(gpEvent);
		}
	}

	/* (non-Javadoc)
	 * @see gaze_library.GazeDataProvider#getTrackerFrequency()
	 */
	@Override
	public int getTrackerFrequency() {
		return trackerFrequency;
	}
}
