package test_planner;
import gaze_library.GazeDataProvider;
import gaze_selection_library.TestManager;
import gaze_selection_library.TestPlanItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * @author Thorsten Bövers
 * This class represents the controller in the model-view-controller hierarchy.
 * It is responsible for the connection between the view and the model. If the view is updated the controller updates the model.
 * This class holds all listeners for the view.
 */
public class TestPlannerController {

	/**
	 * The Java-Logging-Logger.
	 */
	static final Logger log = Logger.getLogger(GazeDataProvider.class.getName());

	/**
	 * Reference for the view.
	 */
	private TestPlannerView view = null;

	/**
	 * Reference for the model.
	 */
	private TestPlannerModel testPlannerModel;

	/**
	 * This variable stores the path to store the model's properties.
	 */
	private String savePath = null;

	/**
	 * Standard constructor which initalizes the look and feel, the model and view, and all listeners on the view.
	 */
	public TestPlannerController() {
		// Set the Windows look and feel
		// If Windows (introduced in Java SE 6) is not available, stay with the default look and feel.
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			log.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			log.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			log.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			log.log(java.util.logging.Level.SEVERE, null, ex);
		}

		this.view = new TestPlannerView();
		this.testPlannerModel = new TestPlannerModel();		

		this.view.setActionListener(new ViewActionListener());
		this.view.setListSelectionListener(new ViewListSelectionListener());
		this.view.setChangeListener(new ViewChangeListener());
	}

	/**
	 * @author Thorsten Bövers
	 * This ActionListener is responsible for the view. It performed the received actions.
	 */
	class ViewActionListener implements ActionListener{
		/*
		 * A button was clicked. Use a switch-case to handle the different buttons.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedItemPosition;
			switch (e.getActionCommand()) {
			case "New":
				savePath = null;
				testPlannerModel.deleteAllTestPlanItems();
				updateListItemsView();
				updateTestRunProperties();				
				break;
			case "Open":
				savePath = view.showOpenFileChooser();
				if (savePath != null) {
					try {						
						// create JAXB context and instantiate unmarshaller
						JAXBContext context = JAXBContext.newInstance(testPlannerModel.getClass());
						Unmarshaller unmarshaller = context.createUnmarshaller();
						// update the model's TestPlanItem list
						//JAXBElement<TestManager> root= unmarshaller.unmarshal(
						System.out.println("Save Path  " + savePath);
						testPlannerModel.set_testPlanItemList(((TestManager) unmarshaller.unmarshal(new FileReader(savePath))).get_testPlanItemList());

						updateListItemsView();
					} catch (FileNotFoundException | JAXBException e1) {
						log.log(java.util.logging.Level.SEVERE, e1.toString());
					}
				}
				break;
			case "Save":
				// if savePath already set (saved already before) override current saved properties,
				// otherwise open dialog window to save them (same as in "save as" option)
				if (savePath == null) {
					savePath = view.showSaveFileChooser();
				}
				if (savePath != null) {
					try {							
						// create JAXB context and instantiate marshaller
						JAXBContext context = JAXBContext.newInstance(testPlannerModel.getClass());
						Marshaller marshaller = context.createMarshaller();
						marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

						// add file type if necessary
						if (!savePath.endsWith(".xml")) {
							savePath += ".xml";
						}
						// Write to File
						marshaller.marshal(testPlannerModel, new File(savePath));							
					} catch (JAXBException e1) {
						log.log(java.util.logging.Level.SEVERE, e1.toString());
					}
				}
				break;
			case "Save As":
				savePath = view.showSaveFileChooser();
				if (savePath != null) {
					try {							
						// create JAXB context and instantiate marshaller
						JAXBContext context = JAXBContext.newInstance(testPlannerModel.getClass());
						Marshaller marshaller = context.createMarshaller();
						marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

						// add file type if necessary
						if (!savePath.endsWith(".xml")) {
							savePath += ".xml";
						}
						// Write to File
						marshaller.marshal(testPlannerModel, new File(savePath));							
					} catch (JAXBException e1) {
						log.log(java.util.logging.Level.SEVERE, e1.toString());
					}
				}
				break;
			case "Run Experiment":
				if (!testPlannerModel.get_testPlanItemList().isEmpty()) {
					int id;
					try {
						id = Integer.valueOf(JOptionPane.showInputDialog("Please enter your participant ID:"));
					} catch (NumberFormatException e2) {
						JOptionPane.showMessageDialog(null,"Please enter a valid number as ID");
						break;
					}
					testPlannerModel.setParticipantID(id);
					testPlannerModel.runTests();
				}
				break;
			case "Add Test...":
				AddTestPanel addTestPanel = new AddTestPanel();
				int i=1;
				int result = JOptionPane.showConfirmDialog(null, addTestPanel, 
						"Test Run Properties", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					testPlannerModel.addTestPlanItem(addTestPanel.getTestPlanItem());
					String path=System.getProperty("user.dir")+"\\"+"Recorded Data"+"\\"+"InputTest"+"\\"+addTestPanel.getTestPlanItem().getGazeDataProviderType()+"-"+addTestPanel.getTestPlanItem().getTargetSelectionType()+"-"+addTestPanel.getTestPlanItem().getTargetSelectionTypeProperties().get(4);
					String FOLDER_PATH= System.getProperty("user.dir")+"\\"+"Recorded Data"+"\\"+"InputTest"+"\\";
					File dir = new File(FOLDER_PATH);				             
					 File[] files = dir.listFiles();
					
					         
					
					        for(File f : files){
					        	if(f.getName().matches(addTestPanel.getTestPlanItem().getGazeDataProviderType()+"-"+addTestPanel.getTestPlanItem().getTargetSelectionType()+"-"+addTestPanel.getTestPlanItem().getTargetSelectionTypeProperties().get(4)))
					        	{
					        		System.out.println("here i "+ i);
					        	i++;
					        	}
					    }

					if (!path.endsWith(".xml")) {
						path += i+".xml";
					}
					JAXBContext context;
					try {
						context = JAXBContext.newInstance(testPlannerModel.getClass());
					
					Marshaller marshaller = context.createMarshaller();
					marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					marshaller.marshal(testPlannerModel, new File(path));
					} catch (JAXBException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					updateListItemsView();
				}				
				break;
			case "Delete Test":
				selectedItemPosition = view.getSelectedListItemIndex();
				if (selectedItemPosition != -1) {
					testPlannerModel.deleteTestPlanItemAtIndex(view.getSelectedListItemIndex());
					updateListItemsView();
					updateTestRunProperties();
				}
				break;
			case "Shift Up":
				int index = view.getSelectedListItemIndex();
				if(index > 0)
				{
					testPlannerModel.switchPositionOfTestPlanItems(index, index-1);
					updateListItemsView();						          
					view.setSelectedListItemIndex(index-1);
				}
				break;
			case "Shift Down":
				index = view.getSelectedListItemIndex();
				if(index < testPlannerModel.get_testPlanItemList().size()-1)
				{
					testPlannerModel.switchPositionOfTestPlanItems(index, index+1);
					updateListItemsView();						          
					view.setSelectedListItemIndex(index+1);
				}
				break;
			case "Set Properties":
				switch (view.getTargetSelectionType().toString()) {
				case "BasicGazeTarget":
					selectedItemPosition = view.getSelectedListItemIndex();
					if (selectedItemPosition != -1) {
						BasicGazeTargetPropertiesPanel basicGazeTargetPropertiesPanel = new BasicGazeTargetPropertiesPanel();						
						basicGazeTargetPropertiesPanel.setBasicGazeTargetProperties(testPlannerModel.get_testPlanItemList().get(selectedItemPosition).getTargetSelectionTypeProperties());
						result = JOptionPane.showConfirmDialog(null, basicGazeTargetPropertiesPanel, 
								"Basic GazeTarget Properties", JOptionPane.OK_CANCEL_OPTION);
						if (result == JOptionPane.OK_OPTION) {
							testPlannerModel.get_testPlanItemList().get(selectedItemPosition).setTargetSelectionTypeProperties(basicGazeTargetPropertiesPanel.getBasicGazeTargetProperties());				
							updateListItemsView();
						}						
						updateTestRunProperties();
						view.setSelectedListItemIndex(selectedItemPosition);
					}
					break;
				case "BasicGazeTargetWithScreenButton":
					selectedItemPosition = view.getSelectedListItemIndex();
					if (selectedItemPosition != -1) {
						BasicGazeTargetPropertiesPanel basicGazeTargetPropertiesPanel = new BasicGazeTargetPropertiesPanel();						
						basicGazeTargetPropertiesPanel.setBasicGazeTargetProperties(testPlannerModel.get_testPlanItemList().get(selectedItemPosition).getTargetSelectionTypeProperties());
						result = JOptionPane.showConfirmDialog(null, basicGazeTargetPropertiesPanel, 
								"Basic GazeTarget Properties", JOptionPane.OK_CANCEL_OPTION);
						if (result == JOptionPane.OK_OPTION) {
							testPlannerModel.get_testPlanItemList().get(selectedItemPosition).setTargetSelectionTypeProperties(basicGazeTargetPropertiesPanel.getBasicGazeTargetProperties());				
							updateListItemsView();
						}						
						updateTestRunProperties();
						view.setSelectedListItemIndex(selectedItemPosition);
					}
					break;
				case "BasicGazeTargetWithGesture":
					selectedItemPosition = view.getSelectedListItemIndex();
					if (selectedItemPosition != -1) {
						BasicGazeTargetPropertiesPanel basicGazeTargetPropertiesPanel = new BasicGazeTargetPropertiesPanel();						
						basicGazeTargetPropertiesPanel.setBasicGazeTargetProperties(testPlannerModel.get_testPlanItemList().get(selectedItemPosition).getTargetSelectionTypeProperties());
						result = JOptionPane.showConfirmDialog(null, basicGazeTargetPropertiesPanel, 
								"Basic GazeTarget Properties", JOptionPane.OK_CANCEL_OPTION);
						if (result == JOptionPane.OK_OPTION) {
							testPlannerModel.get_testPlanItemList().get(selectedItemPosition).setTargetSelectionTypeProperties(basicGazeTargetPropertiesPanel.getBasicGazeTargetProperties());				
							updateListItemsView();
						}						
						updateTestRunProperties();
						view.setSelectedListItemIndex(selectedItemPosition);
					}
					break;
				}
			case "targetSelectionTypeChanged":
				selectedItemPosition = view.getSelectedListItemIndex();
				if (selectedItemPosition != -1) {
					testPlannerModel.get_testPlanItemList().get(selectedItemPosition).setTargetSelectionType(view.getTargetSelectionType());
					view.setListItem(selectedItemPosition, testPlannerModel.get_testPlanItemList().get(selectedItemPosition).propertiesToString());
					view.setSelectedListItemIndex(selectedItemPosition);
				}
				break;
			case "gazeDataProviderTypeChanged":
				selectedItemPosition = view.getSelectedListItemIndex();
				if (selectedItemPosition != -1) {
					testPlannerModel.get_testPlanItemList().get(selectedItemPosition).setGazeDataProviderType(view.getGazeDataProviderType());
					view.setListItem(selectedItemPosition, testPlannerModel.get_testPlanItemList().get(selectedItemPosition).propertiesToString());
					view.setSelectedListItemIndex(selectedItemPosition);
				}
				break;
			case "repetitionTypeChanged":
				selectedItemPosition = view.getSelectedListItemIndex();
				if (selectedItemPosition != -1) {
					testPlannerModel.get_testPlanItemList().get(selectedItemPosition).setRepType(view.getRepType());
					view.setListItem(selectedItemPosition, testPlannerModel.get_testPlanItemList().get(selectedItemPosition).propertiesToString());
					view.setSelectedListItemIndex(selectedItemPosition);
				}
				break;
			}
		}
	}

	/**
	 * @author Thorsten Bövers
	 * This ListSelectionListener is responsible for the view. It performed the received actions.
	 */
	class ViewListSelectionListener implements ListSelectionListener {
		/* 
		 * The state of the jList has changed. Either an item is selected so that the view of Test Run Properties has to be updated
		 * and the buttons to delete or shift the items in the list has to be disabled or no item is selected, so that the buttons
		 * to delete or shift the items in the list has to be disabled.
		 */
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (view.getSelectedListItemIndex() != -1) {
				updateTestRunProperties();
			}
			view.updateEditListItemButtons();

			view.updateExperimentEditButtons();
		}
	}

	/**
	 * @author Thorsten Bövers
	 * This ChangeListener is responsible for the view. It performed the received actions.
	 */
	class ViewChangeListener implements ChangeListener {
		/* 
		 * The number of selections field was edited. Update the model and the view of the jList
		 */
		@Override
		public void stateChanged(ChangeEvent e) {
			int selectedItemPosition = view.getSelectedListItemIndex();
			if (selectedItemPosition != -1) {
				testPlannerModel.get_testPlanItemList().get(selectedItemPosition).setNumberOfSelections(view.getNumSelections());
				view.setListItem(selectedItemPosition, testPlannerModel.get_testPlanItemList().get(selectedItemPosition).propertiesToString());
				view.setSelectedListItemIndex(selectedItemPosition);
			}
		}
	}

	/**
	 * This method updates the list of items on the view regarding to the model.
	 */
	private void updateListItemsView() {
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for (TestPlanItem tpItems : testPlannerModel.get_testPlanItemList()){
			listModel.addElement(tpItems.propertiesToString());
		}
		view.setListModel(listModel);
		view.updateExperimentEditButtons();
	}

	/**
	 * This method updates the TestRunProperties on the view regarding to the selected list item.
	 */
	private void updateTestRunProperties() {
		int selectedItemPosition = view.getSelectedListItemIndex(); 
		if (selectedItemPosition == -1) {
			view.enableTestRunProperties(false);
			view.setDescription("");
		} else {
			view.enableTestRunProperties(true);
			view.setGazeDataProviderType(testPlannerModel.get_testPlanItemList().get(selectedItemPosition).getGazeDataProviderType());
			view.setRepType(testPlannerModel.get_testPlanItemList().get(selectedItemPosition).getRepType());
			view.setTargetSelectionType(testPlannerModel.get_testPlanItemList().get(selectedItemPosition).getTargetSelectionType());
			view.setNumSelections(testPlannerModel.get_testPlanItemList().get(selectedItemPosition).getNumberOfSelections());

			XMLOutputter xmlOutput = new XMLOutputter();	 
			// set XML format
			xmlOutput.setFormat(Format.getPrettyFormat());
			view.setDescription(xmlOutput.outputString(testPlannerModel.get_testPlanItemList().get(selectedItemPosition).getTargetSelectionTypePropertiesXML()));
		}			
	}

}
