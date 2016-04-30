package gaze_selection_library;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;


/**
 * @author Thorsten Bövers
 * This class is used as GazeTargetPresenter for displaying y GazeTarget panel.
 */
public class Window extends JFrame implements GazeTargetPresenter {	

	private static final long serialVersionUID = 5189852262444830416L;

	/**
	 * Standard constructor which creates and displays a JFrame.
	 */
	public Window() {
		
		
		//getContentPane().add(btnNewButton);
		// set window to fullscreen
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		// remove decoration like close-button
		this.setUndecorated(true);
		
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000,500);
		
		// Window in the middle of the screen
		this.setLocationRelativeTo(null);

		// set frame visible
		this.setVisible(true);

	}
	
	/* (non-Javadoc)
	 * @see gaze_selection_library.GazeTargetPresenter#setGazeTarget(gaze_selection_library.GazeTarget)
	 */
	@Override
	public void setGazeTarget(GazeTarget targetPanel) {
		
		
		// add GazeTarget to the panel
		this.add(targetPanel);
		//update the jPanel
		this.validate();
	}
}
