package gaze_selection_library;

import java.util.logging.Level;

import javax.swing.JOptionPane;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;

/**
 * @author Thorsten Bövers
 * This class implements a SelectionTriggerMechanism which generates a trigger event if a keyword, in this case 'click' or 'a',
 * was said by the user.
 * It makes use of the Sphinx-4 library. A good example to understand the functionality is available at:
 * http://www.youtube.com/watch?v=GeqtLrcOogs .
 * Required files are: speechtriggermachanism.config.xml, speech.gram in the /bin/gaze_selection_library/ directory.
 * An Object of this class starts try to start the microphone of the computer.
 * Then it listens to the user and generates a trigger event if the user said the keyword.
 */
public class SpeechTriggerMechanism extends SelectionTriggerMechanism {

	/**
	 * Object of the ConfigurationManager.
	 */
	private ConfigurationManager cm;

	/**
	 * Object of the Recognizer.
	 */
	private Recognizer recognizer;

	/**
	 * Object of the microphone.
	 */
	private Microphone microphone;

	/**
	 * This thread is responsible for listening to the user.
	 */
	private ListenerThread listenerThread;

	/**
	 * Standard constructor which starts the microphone.
	 */
	public SpeechTriggerMechanism() {

		cm = new ConfigurationManager(SpeechTriggerMechanism.class.getResource("speechtriggermechanism.config.xml"));

		recognizer = (Recognizer) cm.lookup("recognizer");
		recognizer.allocate();

		// start the microphone or give an error if this is not possible
		microphone = (Microphone) cm.lookup("microphone");
		if (!microphone.startRecording()) {
			JOptionPane.showMessageDialog(null,"Can't start microphone");
			log.severe(System.currentTimeMillis() + " - " + "Can't start microphone");
			recognizer.deallocate();
		}

		// create a new ListenerThread
		listenerThread = new ListenerThread();
		listenerThread.start();

	}

	/**
	 * @author Thorsten Bövers
	 * This thread is for listening to the microphone if the mechanism was notified. Before this event it sleeps.
	 * After it generates a trigger event, the thread sleeps again until it is notified again.
	 */
	private class ListenerThread extends Thread
	{
		@Override public void run()
		{
			while (true) {
				synchronized (this) {
					try {
						wait();
					} catch (InterruptedException e) {
						log.log(Level.SEVERE,null, e);
					}
					// loop the recognition -> listen to the user
					while (isActivated) {
						synchronized (recognizer) {
							Result result = recognizer.recognize();
							// check if recognized word is equal to the intended keyword						
							if (result != null) {
								String resultText = result.getBestFinalResultNoFiller();
								log.info(System.currentTimeMillis() + " - " + "Analysing spoken word");
								// if the keyword was said -> generate trigger event and notify observers
								if (resultText.equals("click") && isActivated) {
									log.info(System.currentTimeMillis() + " - " + "TriggerEvent generated: Said 'click'");
									notifyAllSelectionTriggerMechanismListener();
								} else if(resultText.equals("a") && isActivated) {
									log.info(System.currentTimeMillis() + " - " + "TriggerEvent generated: Said 'a'");
									notifyAllSelectionTriggerMechanismListener();
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * This method is overridden from the SelectionTriggerMechanism class to add the functionality that the listenerThread
	 * is notified to listen on the microphone.
	 */
	@Override
	public synchronized void setActivated() {
		isActivated = true;
		// only if the listenerThread is not already running, notify it
		if (listenerThread.getState().equals(Thread.State.WAITING)) {
			synchronized (listenerThread) {
				listenerThread.notify();
			}
		}
		
	}

	/**
	 * This method stops the microphone and deallocates the recognizer.
	 */
	public synchronized void release() {
		microphone.stopRecording();
		microphone.clear();
		synchronized (recognizer) {
			recognizer.deallocate();
		}
	}

	/* (non-Javadoc)
	 * @see gaze_selection_library.SelectionTriggerMechanism#setDisabled()
	 */
	@Override
	public synchronized void setDisabled() {
		isActivated = false;				
	}


}
