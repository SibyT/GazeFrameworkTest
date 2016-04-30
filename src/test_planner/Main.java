package test_planner;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author Thorsten Bövers
 * This class is only used to initialize the controller which automatically presents the view.
 * It also initalizes the LogManager.
 * For speech recognition it is important that the speech.gram file is in the /bin/gaze_selection_library/ directory!!!
 */
public class Main {
	
	/**
	 * The Java-Logging-Logger.
	 */
	static final Logger log = Logger.getLogger(Main.class.getName());

	/**
	 * Main function to start the TestPlanner.
	 * @param args
	 */
	public static void main(String[] args) {
		// set the properties for logging
		System.setProperty( "java.util.logging.config.file", "logging.properties" );
		try {
			LogManager.getLogManager().readConfiguration();
			} catch (Exception e) {
				log.log(Level.SEVERE,null, e);
			}
		
		new TestPlannerController();		
	}

}
