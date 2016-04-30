package test_planner;

import javax.xml.bind.annotation.XmlRootElement;

import gaze_selection_library.TestManager;

@XmlRootElement(name = "TestPlannerModel")

/**
 * @author Thorsten Bövers
 * This class extends the TestManager class and represents the model in the model-view-controller hierarchy.
 */
public class TestPlannerModel extends TestManager {

	/**
	 * Standard constructor.
	 */
	public TestPlannerModel() {
		super();
	}

}
