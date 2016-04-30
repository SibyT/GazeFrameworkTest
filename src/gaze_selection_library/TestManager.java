package gaze_selection_library;

import gaze_library.DummyGazeProvider;
import gaze_library.FilteredDataProvider;
import gaze_library.GP3GazeProvider;
import gaze_library.GazeDataProvider;
import gaze_library.TheEyeTribeGazeProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import test_planner.UsabilityTestPanel1;

@XmlRootElement(name = "TestManager")

/**
 * @author Thorsten Bövers
 * This class provides support for automated presentation of targets to test participants and
 * makes use of a GazeTargetPresenter, which is typically a window, for displaying a GazeTarget panel.
 */
public class TestManager {

	/**
	 * The Java-Logging-Logger.
	 */
	static final Logger log = Logger.getLogger(TestManager.class.getName());

	/**
	 * Reference to the gazeTargetPresenter.
	 */
	private Window gazeTargetPresenter;

	/**
	 * Reference to the rawGazeDataProvider.
	 */
	private GazeDataProvider rawGazeDataProvider = null;

	/**
	 * Reference to the filteredDataProvider.
	 */
	private FilteredDataProvider filteredDataProvider = null;

	/**
	 * This variable represents the time in milliseconds until the next intended targetItem is marked on the screen after a TriggerEvent.
	 */
	private int gazeTargetPresentationDelay = 1000;

	/**
	 * List for storing the TestPlanItems.
	 */
	private LinkedList<TestPlanItem> _testPlanItems = new LinkedList<TestPlanItem>();

	/**
	 * Reference to the GazeTarget.
	 */
	private BasicGazeTarget gazeTarget;

	/**
	 * This variable stores the index for the current TestPlanItem.
	 */
	private int currentTestPlanItemIndex;

	/**
	 * This queue stores the indices of the TargetItems in a specified ordering.
	 */
	private Queue<Integer> orderingOfSelectedTargetItems = new LinkedList<Integer>();

	/**
	 * Represents the participant ID.
	 */
	private int participantID = -1;

	/**
	 * Stores a XML representation of the testPlanItem.
	 */
	private Element testPlanItemXML = null;

	/**
	 * This Thread is only used for the selectNextTargetItem method and to speed up the system.
	 */
	private Thread selectNextTargetItemThread = null;

	/**
	 * This Thread is only used for the startWithNextTestPlanItem method and to speed up the system.
	 */
	private Thread startWithNextTestPlanItemThread = null;
	boolean frmUsabilityTest =false;
	private UsabilityTestPanel1 upcomp=null;
	private UsabilityTestCaseForm usabilityTestCaseFormcomp=null;
	private int i2comp;
	private LinkedList<TestPlanItem> tpcomp;
	/**
	 * Standard constructor which initializes and starts the different threads for maximal concurrency.
	 */
	public TestManager() {
		selectNextTargetItemThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					synchronized (this) {					
						try {
							wait();
						} catch (InterruptedException e) {
							log.log(Level.SEVERE,null, e);
						}
						// before selecting the next item wait until the presentation delay is expired
						try {
							Thread.sleep(gazeTargetPresentationDelay);
						} catch (InterruptedException e) {
							log.log(Level.SEVERE,null, e);
						}
						selectNextTargetItem();
					}
				}
			}
		};
		selectNextTargetItemThread.start();

		startWithNextTestPlanItemThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					synchronized (this) {					
						try {
							wait();
						} catch (InterruptedException e) {
							log.log(Level.SEVERE,null, e);
						}
						startWithNextTestPlanItem();
					}
				}
			}
		};
		startWithNextTestPlanItemThread.start();
	}

	/**
	 * This method starts the tests.
	 */
	public void runTests() {
		log.info(System.currentTimeMillis() + " - " + "Run tests");
		currentTestPlanItemIndex = 0;
		synchronized (startWithNextTestPlanItemThread) {
			startWithNextTestPlanItemThread.notify();
			
		}
		
	}

	public void runSingleTest(TestPlanItem currentTestPlanItem, int i2, LinkedList<TestPlanItem> tp, UsabilityTestCaseForm usabilityTestCaseForm){
		System.out.println("type "+ currentTestPlanItem.getType());
this.participantID=usabilityTestCaseForm.getPartipantId();
		i2comp=i2;
		tpcomp=tp;
		usabilityTestCaseFormcomp=usabilityTestCaseForm;
		switch (currentTestPlanItem.getGazeDataProviderType().toString()) {
		case "Dummy":
			rawGazeDataProvider = new DummyGazeProvider(60);
			log.info(System.currentTimeMillis() + " - " + "Created new DummyGazeProvider with " + rawGazeDataProvider.getTrackerFrequency() + " Hz");
			break;
		case "GP3":
			try {
				rawGazeDataProvider = new GP3GazeProvider("127.0.0.1", 4242);
			} catch (UnknownHostException e1) {
				log.log(Level.SEVERE,null, e1);
			} catch (ConnectException e2) {
				log.log(Level.SEVERE,null, e2);
				JOptionPane.showMessageDialog(null,"Please open GazePoint Control first");	
			} catch (IOException e3) {
				log.log(Level.SEVERE,null, e3);
			}
			log.info(System.currentTimeMillis() + " - " + "Created new GP3GazeProvider with " + rawGazeDataProvider.getTrackerFrequency() + " Hz");
			break;
		case "TheEyeTribe":
			try {
				rawGazeDataProvider = new TheEyeTribeGazeProvider("localhost", 6555);
			} catch (UnknownHostException e1) {
				log.log(Level.SEVERE,null, e1);
			} catch (ConnectException e2) {
				log.log(Level.SEVERE,null, e2);
				JOptionPane.showMessageDialog(null,"Please open GazePoint Control first");	
			} catch (IOException e3) {
				log.log(Level.SEVERE,null, e3);
			}
			log.info(System.currentTimeMillis() + " - " + "Created new TheEyeTribeGazeProvider with " + rawGazeDataProvider.getTrackerFrequency() + " Hz");
			break;
		}

		// value is used if GazeTarget is not initialized with specified properties
		int numberOfButtonsOnGazeTarget = 6;
		Random randomNumberGenerator = new Random();

		// initialize the GazeTarget
		switch (currentTestPlanItem.getTargetSelectionType().toString()) {
		case "BasicGazeTarget":
			if (currentTestPlanItem.getTargetSelectionTypeProperties() == null) {
				gazeTarget = new BasicGazeTarget(this,rawGazeDataProvider,filteredDataProvider);					
			} else {
				System.out.println("xoxo");
				gazeTarget = new BasicGazeTarget(this,rawGazeDataProvider,filteredDataProvider,currentTestPlanItem);
				// get number of buttons on the GazeTarget
				numberOfButtonsOnGazeTarget = Integer.valueOf(currentTestPlanItem.getTargetSelectionTypeProperties().get(1));
			}
			log.info(System.currentTimeMillis() + " - " + "Created new BasicGazeTarget");
			break;
		case "BasicGazeTargetWithScreenButton":
			if (currentTestPlanItem.getTargetSelectionTypeProperties() == null) {
				gazeTarget = new BasicGazeTargetWithScreenButton(this,rawGazeDataProvider,filteredDataProvider);					
			} else {
				gazeTarget = new BasicGazeTargetWithScreenButton(this,rawGazeDataProvider,filteredDataProvider,currentTestPlanItem.getTargetSelectionTypeProperties());
				// get number of buttons on the GazeTarget
				numberOfButtonsOnGazeTarget = Integer.valueOf(currentTestPlanItem.getTargetSelectionTypeProperties().get(1));
			}
			log.info(System.currentTimeMillis() + " - " + "Created new BasicGazeTargetWithScreenButton");
			break;
		case "BasicGazeTargetWithGesture":
			if (currentTestPlanItem.getTargetSelectionTypeProperties() == null) {
				gazeTarget = new BasicGazeTargetWithGesture(this,rawGazeDataProvider,filteredDataProvider);					
			} else {
				gazeTarget = new BasicGazeTargetWithGesture(this,rawGazeDataProvider,filteredDataProvider,currentTestPlanItem.getTargetSelectionTypeProperties());
				// get number of buttons on the GazeTarget
				numberOfButtonsOnGazeTarget = Integer.valueOf(currentTestPlanItem.getTargetSelectionTypeProperties().get(1));
			}
			log.info(System.currentTimeMillis() + " - " + "Created new BasicGazeTargetWithGesture");
			break;
		}			

		// create the ordering of the to selected TargetItems
		switch (currentTestPlanItem.getRepType().toString()) {
		case "RANDOM":
			for (int i=0; i<currentTestPlanItem.getNumberOfSelections(); i++) {
				orderingOfSelectedTargetItems.add(randomNumberGenerator.nextInt(numberOfButtonsOnGazeTarget));
			}
			break;
		case "SEQUENTIAL":
			for (int i=0; i<currentTestPlanItem.getNumberOfSelections(); i++) {
				// modulo to avoid indices greater than the number of target items on the screen
				orderingOfSelectedTargetItems.add(i % numberOfButtonsOnGazeTarget);
			}
			break;
		case "REVERSE_SEQUENTIAL":
			int counter=currentTestPlanItem.getNumberOfSelections();
			while (counter>0) {
				for (int j=numberOfButtonsOnGazeTarget-1; j>=0 && counter>0; j--) {
					orderingOfSelectedTargetItems.add(j);
					counter--;
				}
			}
			break;
		case "RANDOM_NO_SUCCESSIVE_REPEATS":
			int priorRandomNumber = -1;
			int currentRandomNumber = -1;
			for (int i=0; i<currentTestPlanItem.getNumberOfSelections(); i++) {
				currentRandomNumber = randomNumberGenerator.nextInt(numberOfButtonsOnGazeTarget);
				while (currentRandomNumber == priorRandomNumber) {
					currentRandomNumber = randomNumberGenerator.nextInt(numberOfButtonsOnGazeTarget);
				}
				orderingOfSelectedTargetItems.add(currentRandomNumber);
				priorRandomNumber = currentRandomNumber;
			}
			break;
		case "RANDOM_NO_REPEATS":
			LinkedList<Integer> alreadyAddedRandomNumbers = new LinkedList<Integer>();
			int randomNumber = -1;
			for (int i=0; i<currentTestPlanItem.getNumberOfSelections(); i++) {
				if (alreadyAddedRandomNumbers.size() == numberOfButtonsOnGazeTarget) {
					alreadyAddedRandomNumbers.clear();
				}
				do {
					randomNumber = randomNumberGenerator.nextInt(numberOfButtonsOnGazeTarget);
				}
				while (alreadyAddedRandomNumbers.contains(randomNumber));
				alreadyAddedRandomNumbers.add(randomNumber);
				orderingOfSelectedTargetItems.add(randomNumber);
			}
			break;
		}
		testPlanItemXML = currentTestPlanItem.toXML();
		testPlanItemXML.setAttribute(new Attribute("index", String.valueOf(currentTestPlanItemIndex)));	
			

		// initialize the GazeTargetPresenter and add the GazeTarget
		gazeTargetPresenter = new Window();			
		gazeTargetPresenter.setGazeTarget(gazeTarget);
		


		//System.out.println("orderingOfSelectedTargetItemsdd" +orderingOfSelectedTargetItems);

		//i2++;
		
			synchronized (selectNextTargetItemThread){

				selectNextTargetItemThread.notify();
				
				
			};
			frmUsabilityTest=true;
			
			
			
			
		
	

	}
	/**
	 * This method either starts with presenting the next TestPlanItem or if all TestPlanItems were presented, calls the testsAllCompleted method.
	 */
	private void startWithNextTestPlanItem() {

		if (currentTestPlanItemIndex < _testPlanItems.size()) {

			log.info(System.currentTimeMillis() + " - " + "Start with next TestPlanItem: (" + (currentTestPlanItemIndex+1) + " of " + _testPlanItems.size() + ")");

			TestPlanItem currentTestPlanItem = _testPlanItems.get(currentTestPlanItemIndex);	

			// initialize the GazeDataProvider
			switch (currentTestPlanItem.getGazeDataProviderType().toString()) {
			case "Dummy":
				rawGazeDataProvider = new DummyGazeProvider(60);
				log.info(System.currentTimeMillis() + " - " + "Created new DummyGazeProvider with " + rawGazeDataProvider.getTrackerFrequency() + " Hz");
				break;
			case "GP3":
				try {
					rawGazeDataProvider = new GP3GazeProvider("127.0.0.1", 4242);
				} catch (UnknownHostException e1) {
					log.log(Level.SEVERE,null, e1);
				} catch (ConnectException e2) {
					log.log(Level.SEVERE,null, e2);
					JOptionPane.showMessageDialog(null,"Please open GazePoint Control first");	
				} catch (IOException e3) {
					log.log(Level.SEVERE,null, e3);
				}
				log.info(System.currentTimeMillis() + " - " + "Created new GP3GazeProvider with " + rawGazeDataProvider.getTrackerFrequency() + " Hz");
				break;
			case "TheEyeTribe":
				try {
					rawGazeDataProvider = new TheEyeTribeGazeProvider("localhost", 6555);
				} catch (UnknownHostException e1) {
					log.log(Level.SEVERE,null, e1);
				} catch (ConnectException e2) {
					log.log(Level.SEVERE,null, e2);
					JOptionPane.showMessageDialog(null,"Please open GazePoint Control first");	
				} catch (IOException e3) {
					log.log(Level.SEVERE,null, e3);
				}
				log.info(System.currentTimeMillis() + " - " + "Created new TheEyeTribeGazeProvider with " + rawGazeDataProvider.getTrackerFrequency() + " Hz");
				break;
			}

			// value is used if GazeTarget is not initialized with specified properties
			int numberOfButtonsOnGazeTarget = 6;
			Random randomNumberGenerator = new Random();

			// initialize the GazeTarget
			switch (currentTestPlanItem.getTargetSelectionType().toString()) {
			case "BasicGazeTarget":
				if (currentTestPlanItem.getTargetSelectionTypeProperties() == null) {
					gazeTarget = new BasicGazeTarget(this,rawGazeDataProvider,filteredDataProvider);					
				} else {
					gazeTarget = new BasicGazeTarget(this,rawGazeDataProvider,filteredDataProvider,currentTestPlanItem.getTargetSelectionTypeProperties());
					// get number of buttons on the GazeTarget
					numberOfButtonsOnGazeTarget = Integer.valueOf(currentTestPlanItem.getTargetSelectionTypeProperties().get(1));
				}
				log.info(System.currentTimeMillis() + " - " + "Created new BasicGazeTarget");
				break;
			case "BasicGazeTargetWithScreenButton":
				if (currentTestPlanItem.getTargetSelectionTypeProperties() == null) {
					gazeTarget = new BasicGazeTargetWithScreenButton(this,rawGazeDataProvider,filteredDataProvider);					
				} else {
					gazeTarget = new BasicGazeTargetWithScreenButton(this,rawGazeDataProvider,filteredDataProvider,currentTestPlanItem.getTargetSelectionTypeProperties());
					// get number of buttons on the GazeTarget
					numberOfButtonsOnGazeTarget = Integer.valueOf(currentTestPlanItem.getTargetSelectionTypeProperties().get(1));
				}
				log.info(System.currentTimeMillis() + " - " + "Created new BasicGazeTargetWithScreenButton");
				break;
			case "BasicGazeTargetWithGesture":
				if (currentTestPlanItem.getTargetSelectionTypeProperties() == null) {
					gazeTarget = new BasicGazeTargetWithGesture(this,rawGazeDataProvider,filteredDataProvider);					
				} else {
					gazeTarget = new BasicGazeTargetWithGesture(this,rawGazeDataProvider,filteredDataProvider,currentTestPlanItem.getTargetSelectionTypeProperties());
					// get number of buttons on the GazeTarget
					numberOfButtonsOnGazeTarget = Integer.valueOf(currentTestPlanItem.getTargetSelectionTypeProperties().get(1));
				}
				log.info(System.currentTimeMillis() + " - " + "Created new BasicGazeTargetWithGesture");
				break;
			}			

			// create the ordering of the to selected TargetItems
			switch (currentTestPlanItem.getRepType().toString()) {
			case "RANDOM":
				for (int i=0; i<currentTestPlanItem.getNumberOfSelections(); i++) {
					orderingOfSelectedTargetItems.add(randomNumberGenerator.nextInt(numberOfButtonsOnGazeTarget));
				}
				break;
			case "SEQUENTIAL":
				for (int i=0; i<currentTestPlanItem.getNumberOfSelections(); i++) {
					// modulo to avoid indices greater than the number of target items on the screen
					orderingOfSelectedTargetItems.add(i % numberOfButtonsOnGazeTarget);
				}
				break;
			case "REVERSE_SEQUENTIAL":
				int counter=currentTestPlanItem.getNumberOfSelections();
				while (counter>0) {
					for (int j=numberOfButtonsOnGazeTarget-1; j>=0 && counter>0; j--) {
						orderingOfSelectedTargetItems.add(j);
						counter--;
					}
				}
				break;
			case "RANDOM_NO_SUCCESSIVE_REPEATS":
				int priorRandomNumber = -1;
				int currentRandomNumber = -1;
				for (int i=0; i<currentTestPlanItem.getNumberOfSelections(); i++) {
					currentRandomNumber = randomNumberGenerator.nextInt(numberOfButtonsOnGazeTarget);
					while (currentRandomNumber == priorRandomNumber) {
						currentRandomNumber = randomNumberGenerator.nextInt(numberOfButtonsOnGazeTarget);
					}
					orderingOfSelectedTargetItems.add(currentRandomNumber);
					priorRandomNumber = currentRandomNumber;
				}
				break;
			case "RANDOM_NO_REPEATS":
				LinkedList<Integer> alreadyAddedRandomNumbers = new LinkedList<Integer>();
				int randomNumber = -1;
				for (int i=0; i<currentTestPlanItem.getNumberOfSelections(); i++) {
					if (alreadyAddedRandomNumbers.size() == numberOfButtonsOnGazeTarget) {
						alreadyAddedRandomNumbers.clear();
					}
					do {
						randomNumber = randomNumberGenerator.nextInt(numberOfButtonsOnGazeTarget);
					}
					while (alreadyAddedRandomNumbers.contains(randomNumber));
					alreadyAddedRandomNumbers.add(randomNumber);
					orderingOfSelectedTargetItems.add(randomNumber);
				}
				break;
			}

			// instantiate document for storing data records
			testPlanItemXML = currentTestPlanItem.toXML();
			testPlanItemXML.setAttribute(new Attribute("index", String.valueOf(currentTestPlanItemIndex)));		

			// initialize the GazeTargetPresenter and add the GazeTarget
			gazeTargetPresenter = new Window();			
			gazeTargetPresenter.setGazeTarget(gazeTarget);
			log.info(System.currentTimeMillis() + " - " + "Created new GazeTargetPresenter and set current GazeTarget");
			currentTestPlanItemIndex++;

			synchronized (selectNextTargetItemThread) {
				selectNextTargetItemThread.notify();
			}

		} else {			
			testsAllCompleted();	
		
		}
	}

	/**
	 * This method either selects the next TargetItem which has to be triggered or if all TargetItems has been selected starts to write the recorded data into an XML element
	 */
	private void selectNextTargetItem() {

		if (!orderingOfSelectedTargetItems.isEmpty()) {			
			gazeTarget.setTargetIndex(orderingOfSelectedTargetItems.remove());
			log.info(System.currentTimeMillis() + " - " + "Selected the next intended target item on the GazeTargetPanel");
		} else {
			log.info(System.currentTimeMillis() + " - " + "All intended target items has been presented");
			// store data records for current testPlanItem
			Document xmlDocument = new Document(testPlanItemXML);
			XMLOutputter xmlOutput = new XMLOutputter();	 
			// display in a nice format
			xmlOutput.setFormat(Format.getPrettyFormat());
			try {
				File folder = new File("Recorded Data");
				folder.mkdir();
				xmlOutput.output(xmlDocument, new FileWriter("Recorded Data\\" + participantID + "-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "-" + testPlanItemXML.getAttributeValue("index") + ".xml"));
			} catch (IOException e) {
				log.log(Level.SEVERE,null, e);
			}
			log.info(System.currentTimeMillis() + " - " + "Wrote recorded data to file for current TestPlanItem");
			xmlOutput = null;
			testPlanItemXML = null;


			// delete all references from gazeTarget, DataProviders and Windows
			gazeTargetPresenter.dispose();
			if (rawGazeDataProvider != null) {
				try {
					rawGazeDataProvider.closeGazeDataProvider();
				} catch (Exception e) {
					log.log(Level.SEVERE,null, e);
				}
				rawGazeDataProvider = null;
			}
			if (filteredDataProvider != null) {
				try {
					filteredDataProvider.closeGazeDataProvider();
				} catch (Exception e) {
					log.log(Level.SEVERE,null, e);
				}
				filteredDataProvider = null;
			}
			gazeTargetPresenter = null;
			gazeTarget.release();
			gazeTarget = null;

			synchronized (startWithNextTestPlanItemThread) {
				startWithNextTestPlanItemThread.notify();		
			}
		}
	}

	/**
	 * This method is called if all tests were completed.
	 */
	private void testsAllCompleted() {
		log.info(System.currentTimeMillis() + " - " + "All scheduled tests completed");
		currentTestPlanItemIndex = 0;
		if(frmUsabilityTest){
		 upcomp = new UsabilityTestPanel1();
		
		upcomp.question(i2comp, tpcomp, usabilityTestCaseFormcomp,frmUsabilityTest);
			
	}
	}

	/**
	 * This function adds the referenced TestPlanItem to the end of the list.
	 * @param testPlanItem
	 */
	public void addTestPlanItem(TestPlanItem testPlanItem) {
		_testPlanItems.addLast(testPlanItem);
	}

	/**
	 * This function deletes the TestPlanItem at the given index from the list.
	 * @param index of TestPlanItem in list
	 */
	public void deleteTestPlanItemAtIndex(int index) {
		_testPlanItems.remove(index);
	}

	/**
	 * This function clears the TestPlanItem list.
	 */
	public void deleteAllTestPlanItems() {
		_testPlanItems.clear();
	}

	/**
	 * Getter for the TestPlanItem list.
	 * @return the _testPlanItem
	 */
	@XmlElement(name = "TestPlanItem")
	public LinkedList<TestPlanItem> get_testPlanItemList() {
		return _testPlanItems;
	}

	/**
	 * Setter for the TestPlanItem list.
	 * @param _testPlanItemList the _testPlanItem to set
	 */
	public void set_testPlanItemList(LinkedList<TestPlanItem> _testPlanItemList) {
		this._testPlanItems = _testPlanItemList;
	}

	/**
	 * This function switches the positions of two given TestPlanItems with each other.
	 * @param index1
	 * @param index2
	 */
	public void switchPositionOfTestPlanItems(int index1, int index2) {
		_testPlanItems.add(index2,_testPlanItems.remove(index1));
	}

	/**
	 * Getter for the presentation delay.
	 * @return the TargetPresentationDelay
	 */
	public int getTargetPresentationDelay() {
		return gazeTargetPresentationDelay;
	}
	
	/**
	 * Setter for the presentation delay.
	 * @param gazeTargetPresentationDelay
	 */
	public void setTargetPresentationDelay(int gazeTargetPresentationDelay) {
		this.gazeTargetPresentationDelay = gazeTargetPresentationDelay;
	}

	/**
	 * This method is called by the GazeTarget to notify the TestManager that presenting the GazeTarget is finished.
	 * It adds the referenced XML element which contains the recorded data to the XML document.
	 * @param element as a XML representation of the recorded data
	 */
	public void recordingDataInGazeTargetFinished(Element element) {	
		testPlanItemXML.addContent(element);

		synchronized (selectNextTargetItemThread) {
			selectNextTargetItemThread.notify();
		}		
	}

	/**
	 * Getter for the TestPlanItem index.
	 * @return the index of the current TestPlanItem
	 */
	public String getTestPlanItemIndex() {
		return testPlanItemXML.getAttributeValue("index");
	}

	/**
	 * Setter for the participant ID.
	 * @param id
	 */
	public void setParticipantID(int id) {
		participantID = id;
	}

	/**
	 * Getter for the participant ID.
	 * @return the participant ID
	 */
	@XmlTransient
	public int getParticipantID() {
		return participantID;		
	}
}
