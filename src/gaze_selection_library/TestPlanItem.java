package gaze_selection_library;

import gaze_library.GazeDataProviderType;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import org.jdom2.Element;

@XmlRootElement(name = "TestPlanItem")

/**
 * @author Thorsten Bövers
 * A TestPlanItem represents a set of trials for a particular interaction technique.
 */
public class TestPlanItem {

	/**
	 * Stores the GazeDataProviderType
	 */
	private GazeDataProviderType gazeDataProviderType;
	
	/**
	 * Stores the repetitionType
	 */
	private RepetitionType repetitionType;
	
	/**
	 * Stores the targetSelectionType
	 */
	private TargetSelectionType targetSelectionType;
	
	/**
	 * Stores the targetSelectionTypeProperties
	 */
	private LinkedList<String> targetSelectionTypeProperties;
	
	/**
	 * Stores the numberOfSelections
	 */
	private int numberOfSelections;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String type;
	
	/**
	 * Empty standard constructor. Needed for JAXB.
	 */
	public TestPlanItem() {
		
	}

	/**
	 * Standard constructor which takes all properties as input.
	 * @param gazeDataProviderType
	 * @param targetSelectionType
	 * @param targetSelectionTypeProperties
	 * @param numberOfSelections
	 * @param repType
	 */
	public TestPlanItem(GazeDataProviderType gazeDataProviderType, TargetSelectionType targetSelectionType, LinkedList<String> targetSelectionTypeProperties, int numberOfSelections, RepetitionType repType,String type) {
		this.setGazeDataProviderType(gazeDataProviderType);
		this.targetSelectionType = targetSelectionType;
		this.targetSelectionTypeProperties = targetSelectionTypeProperties;
		this.numberOfSelections = numberOfSelections;
		this.repetitionType = repType;
		this.type = type;


	}
	
	/**
	 * @return the gazeDataProviderType
	 */
	public GazeDataProviderType getGazeDataProviderType() {
		return gazeDataProviderType;
	}

	/**
	 * @param gazeDataProviderType the gazeDataProviderType to set
	 */
	public void setGazeDataProviderType(GazeDataProviderType gazeDataProviderType) {
		this.gazeDataProviderType = gazeDataProviderType;
	}

	/**
	 * @return the repType
	 */
	public TargetSelectionType getTargetSelectionType() {
		return targetSelectionType;
	}

	/**
	 * @param repetitionType the repType to set
	 */
	public void setTargetSelectionType(TargetSelectionType targetSelectionType) {
		this.targetSelectionType = targetSelectionType;
	}
	
	/**
	 * @return the targetSelectionTypeProperties
	 */
	public LinkedList<String> getTargetSelectionTypeProperties() {
		return targetSelectionTypeProperties;
	}
	
	/**
	 * @return the targetSelectionTypeProperties as XML element
	 */
	public Element getTargetSelectionTypePropertiesXML() {
		Element propertiesXML = null;
		// depending on the TargetSelectionType create another XML representation
		switch (targetSelectionType.toString()) {
		case "BasicGazeTarget":
			propertiesXML = new Element("BasicGazeTarget");
			propertiesXML.addContent(new Element("FeedbackCursor").setText(targetSelectionTypeProperties.get(0)));
			propertiesXML.addContent(new Element("NumberOfButtons").setText(targetSelectionTypeProperties.get(1)));
			propertiesXML.addContent(new Element("ButtonHeightRatio").setText(targetSelectionTypeProperties.get(2)));
			propertiesXML.addContent(new Element("DeadSpace").setText(targetSelectionTypeProperties.get(3)));
			propertiesXML.addContent(new Element("SelectionTriggerType").setText(targetSelectionTypeProperties.get(4)));
			switch (targetSelectionTypeProperties.get(4)) {
			case "DwellTime":
				propertiesXML.addContent(new Element("DwellTime").setText(targetSelectionTypeProperties.get(5)));
				propertiesXML.addContent(new Element("Accuracy").setText(targetSelectionTypeProperties.get(6)));
				break;
			case "Wink":
				propertiesXML.addContent(new Element("WinkTime").setText(targetSelectionTypeProperties.get(5)));
				break;
			}
			break;
		case "BasicGazeTargetWithScreenButton":
			propertiesXML = new Element("BasicGazeTargetWithScreenButton");
			propertiesXML.addContent(new Element("FeedbackCursor").setText(targetSelectionTypeProperties.get(0)));
			propertiesXML.addContent(new Element("NumberOfButtons").setText(targetSelectionTypeProperties.get(1)));
			propertiesXML.addContent(new Element("ButtonHeightRatio").setText(targetSelectionTypeProperties.get(2)));
			propertiesXML.addContent(new Element("DeadSpace").setText(targetSelectionTypeProperties.get(3)));
			propertiesXML.addContent(new Element("SelectionTriggerType").setText(targetSelectionTypeProperties.get(4)));
			switch (targetSelectionTypeProperties.get(4)) {
			case "DwellTime":
				propertiesXML.addContent(new Element("DwellTime").setText(targetSelectionTypeProperties.get(5)));
				propertiesXML.addContent(new Element("Accuracy").setText(targetSelectionTypeProperties.get(6)));
				break;
			case "Wink":
				propertiesXML.addContent(new Element("WinkTime").setText(targetSelectionTypeProperties.get(5)));
				break;
			}
			break;
		case "BasicGazeTargetWithGesture":
			propertiesXML = new Element("BasicGazeTargetWithGesture");
			propertiesXML.addContent(new Element("FeedbackCursor").setText(targetSelectionTypeProperties.get(0)));
			propertiesXML.addContent(new Element("NumberOfButtons").setText(targetSelectionTypeProperties.get(1)));
			propertiesXML.addContent(new Element("ButtonHeightRatio").setText(targetSelectionTypeProperties.get(2)));
			propertiesXML.addContent(new Element("DeadSpace").setText(targetSelectionTypeProperties.get(3)));
			propertiesXML.addContent(new Element("SelectionTriggerType").setText(targetSelectionTypeProperties.get(4)));
			switch (targetSelectionTypeProperties.get(4)) {
			case "DwellTime":
				propertiesXML.addContent(new Element("DwellTime").setText(targetSelectionTypeProperties.get(5)));
				propertiesXML.addContent(new Element("Accuracy").setText(targetSelectionTypeProperties.get(6)));
				break;
			case "Wink":
				propertiesXML.addContent(new Element("WinkTime").setText(targetSelectionTypeProperties.get(5)));
				break;
			}
			break;
		}
		return propertiesXML;

	}

	/**
	 * @param targetSelectionTypeProperties the targetSelectionTypeProperties to set
	 */
	public void setTargetSelectionTypeProperties(LinkedList<String> targetSelectionTypeProperties) {
		this.targetSelectionTypeProperties = targetSelectionTypeProperties;
	}

	/**
	 * @return the repType
	 */
	public RepetitionType getRepType() {
		return repetitionType;
	}

	/**
	 * @param repType the repType to set
	 */
	public void setRepType(RepetitionType repType) {
		this.repetitionType = repType;
	}

	/**
	 * @return the numberOfSelections
	 */
	public int getNumberOfSelections() {
		return numberOfSelections;
	}

	/**
	 * @param numberOfSelections the numberOfSelections to set
	 */
	public void setNumberOfSelections(int numberOfSelections) {
		this.numberOfSelections = numberOfSelections;
	}

	/**
	 * @return the TestPlanItem as XML element included the TargetSelectionProperties
	 */
	public Element toXML() {		
		Element testPlanItemXML = new Element("TestPlanItem");
		
		testPlanItemXML.addContent(new Element("GazeDataProviderType").setText(String.valueOf(gazeDataProviderType)));
		testPlanItemXML.addContent(getTargetSelectionTypePropertiesXML());
		testPlanItemXML.addContent(new Element("RepetitionType").setText(String.valueOf(repetitionType)));
		testPlanItemXML.addContent(new Element("NumberOfSelections").setText(String.valueOf(numberOfSelections)));
		
		return testPlanItemXML;
	}

	/**
	 * @return the TestPlanItem properties as string for the list representation on the GUI
	 */
	public String propertiesToString() {
		String properties = "Provider:" + this.gazeDataProviderType + " Target:" + this.targetSelectionType + " NumSelections:" + this.numberOfSelections + " RepType:" + this.repetitionType;
		return properties;
	}
	
}
