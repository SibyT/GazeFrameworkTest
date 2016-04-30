package gaze_selection_library;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;

/**
 * @author Thorsten Bövers
 * This class implements a SelectionTriggerMechanism which reacts on a key trigger.
 * In this special implementation a trigger event is generated if the key 'c' on the keyboard is pressed.
 */
public class KeyboardTriggerMechanism extends SelectionTriggerMechanism implements KeyListener {
	
	private JComponent component;

	/**
	 * Standard constructor
	 * @param component as JComponent to register the KeyListener at the specific JComponent (e.g. JPanel=GazeTarget)
	 */
	public KeyboardTriggerMechanism(JComponent component) {
		this.component = component;		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public synchronized void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyChar() == 'c' && isActivated) {
			log.info(System.currentTimeMillis() + " - " + "TriggerEvent generated: Key_C pressed");
			notifyAllSelectionTriggerMechanismListener();
			component.removeKeyListener(this);
		}		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {	
	}
	
	/*
	 * (non-Javadoc)
	 * @see gaze_selection_library.SelectionTriggerMechanism#setActivated()
	 */
	@Override
	public synchronized void setActivated() {		
		component.addKeyListener(this);		
		isActivated = true;
	}

	/* (non-Javadoc)
	 * @see gaze_selection_library.SelectionTriggerMechanism#setDisabled()
	 */
	@Override
	public synchronized void setDisabled() {
		isActivated = false;
		component.removeKeyListener(this);		
	}

}
