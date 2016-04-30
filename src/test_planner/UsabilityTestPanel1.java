package test_planner;

import gaze_library.DummyGazeProvider;
import gaze_library.FilteredDataProvider;
import gaze_library.GazeDataProvider;
import gaze_library.GazeDataProviderType;
import gaze_selection_library.BasicGazeTarget;
import gaze_selection_library.BasicGazeTargetWithScreenButton;
import gaze_selection_library.RepetitionType;
import gaze_selection_library.TargetSelectionType;
import gaze_selection_library.TestManager;
import gaze_selection_library.TestPlanItem;
import gaze_selection_library.UsabilityTestCaseForm;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;

import javax.swing.JSplitPane;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.JTextField;

import java.awt.GridLayout;

import javax.swing.BoxLayout;

import java.awt.Canvas;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerDateModel;

import java.util.Date;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Window.Type;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.awt.Frame;
import java.awt.List;
import java.awt.Label;
import java.awt.Choice;

import javax.swing.ImageIcon;

public class UsabilityTestPanel1 {
	private JFrame frmTestForUsability;
	private JTextField userName_txt;
	private JTextField age_txt;
	private GazeDataProvider rawGazeDataProvider = null;
	private BasicGazeTarget gazeTarget;
	private FilteredDataProvider filteredDataProvider = null;
	
	
	static final Logger log = Logger.getLogger(TestManager.class.getName());
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UsabilityTestPanel1 window = new UsabilityTestPanel1();
					
					window.frmTestForUsability.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UsabilityTestPanel1() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		 final Question ques = null;

		
		final UsabilityTestCaseForm  usabilityTestCaseForm= new UsabilityTestCaseForm();
		frmTestForUsability = new JFrame();
		frmTestForUsability.setTitle("Test For Usability");
		frmTestForUsability.setBounds(100, 100, 450, 300);
		frmTestForUsability.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTestForUsability.setExtendedState(Frame.MAXIMIZED_BOTH);
	
		frmTestForUsability.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("User Name");
		lblNewLabel.setToolTipText("User Name");
		lblNewLabel.setBounds(10, 11, 140, 14);
		

		frmTestForUsability.getContentPane().add(lblNewLabel);
		
		userName_txt = new JTextField();
		userName_txt.setToolTipText("User Name");
		userName_txt.setBounds(215, 8, 209, 20);
		frmTestForUsability.getContentPane().add(userName_txt);
		userName_txt.setColumns(10);
		userName_txt.setInputVerifier(new UsabilityInputVerifier());
		
		JLabel lblNewLabel_1 = new JLabel("Age");
		lblNewLabel_1.setToolTipText("Age");
		lblNewLabel_1.setBounds(10, 38, 46, 14);
		frmTestForUsability.getContentPane().add(lblNewLabel_1);
		
		age_txt = new JTextField();
		age_txt.setToolTipText("Age");
		age_txt.setBounds(215, 35, 209, 20);
		frmTestForUsability.getContentPane().add(age_txt);
		age_txt.setColumns(10);
		age_txt.setInputVerifier(new UsabilityInputVerifier());
		JLabel lblNewLabel_2 = new JLabel("Gender");
		lblNewLabel_2.setToolTipText("Gender");
		lblNewLabel_2.setBounds(10, 63, 46, 14);
		frmTestForUsability.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Gaze Data Provider Type");
		lblNewLabel_3.setToolTipText("Gaze Data Provider Type");
		lblNewLabel_3.setBounds(10, 88, 151, 14);
		frmTestForUsability.getContentPane().add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Trigger Type");
		lblNewLabel_4.setToolTipText("Trigger Type");
		lblNewLabel_4.setBounds(10, 138, 114, 14);
		frmTestForUsability.getContentPane().add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Test Date");
		lblNewLabel_5.setToolTipText("Test Date");
		lblNewLabel_5.setBounds(10, 163, 78, 14);
		frmTestForUsability.getContentPane().add(lblNewLabel_5);

		final JSpinner Gender = new JSpinner();
		Gender.setToolTipText("Gender");
		Gender.setModel(new SpinnerListModel(new String[] {"Male", "Female"}));
		Gender.setBounds(215, 60, 209, 20);
		frmTestForUsability.getContentPane().add(Gender);
		
		JSpinner spinner_2 = new JSpinner();
		spinner_2.setToolTipText("Test Date");
		spinner_2.setModel(new SpinnerDateModel(new Date(1445814000000L), new Date(1445814000000L), null, Calendar.DAY_OF_YEAR));
		spinner_2.setBounds(215, 160, 209, 20);
		frmTestForUsability.getContentPane().add(spinner_2);
		
		JButton startButton = new JButton("Start");
		startButton.setToolTipText("Submit");
		
		
		startButton.setBounds(105, 215, 114, 23);
		frmTestForUsability.getContentPane().add(startButton);
		
		final JSpinner spinner = new JSpinner();
		spinner.setToolTipText("Trigger Type");
		spinner.setModel(new SpinnerListModel(new String[] {"KeyBoardKeyC", "DwellTime", "Wink", "Speech"}));
		spinner.setBounds(215, 135, 209, 20);
		frmTestForUsability.getContentPane().add(spinner);
		
		final JSpinner spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerListModel(new String[] {"Dummy", "GP3", "TheEyeTribe"}));
		spinner_1.setToolTipText("Gaze Data Provider");
		spinner_1.setBounds(215, 85, 209, 20);
		frmTestForUsability.getContentPane().add(spinner_1);
		
		JLabel label = new JLabel("Target Selection Type");
		label.setBounds(10, 108, 140, 22);
		frmTestForUsability.getContentPane().add(label);
		
		final JSpinner spinner_3 = new JSpinner();
		spinner_3.setModel(new SpinnerListModel(new String[] {"BasicGazeTarget", "BasicGazeTargetWithScreenButton"}));
		spinner_3.setToolTipText("Target Selection Type");
		spinner_3.setBounds(215, 108, 209, 20);
		frmTestForUsability.getContentPane().add(spinner_3);
		
		JLabel lblParticipantId = new JLabel("Participant Id");
		lblParticipantId.setBounds(10, 188, 114, 14);
		frmTestForUsability.getContentPane().add(lblParticipantId);
		
		textField = new JTextField();
		textField.setBounds(215, 185, 209, 20);
		frmTestForUsability.getContentPane().add(textField);
		textField.setColumns(10);
		
		
		
		startButton.addActionListener(new ActionListener() {
			private TestPlannerModel testPlannerModel = new TestPlannerModel ();

			public void actionPerformed(ActionEvent e) {
				//Question ques=null;
//				UsabilityInputVerifier inputVerifier= new UsabilityInputVerifier();
//				if(!inputVerifier.verify(userName_txt) ||!inputVerifier.verify(age_txt) ||!inputVerifier.verify(eyeGear_text) ){
//					JOptionPane.showMessageDialog(null, "Please enter key Values " );
//				}else{
				usabilityTestCaseForm.setGazeDataProviderType(spinner_1.getValue().toString());
				usabilityTestCaseForm.setTargetSelectionType(spinner_3.getValue().toString());
				usabilityTestCaseForm.setTriggerType(spinner.getValue().toString());
				if(textField.getText()==null || textField.getText().equalsIgnoreCase("")){
					JOptionPane.showMessageDialog(null, "Please enter key Values " );
				}else{
					frmTestForUsability.setVisible(false);

				usabilityTestCaseForm.setPartipantId(Integer.valueOf(textField.getText()));
				
				this.testPlannerModel = new TestPlannerModel();	
					File folder = new File("E:\\Eclipse Project\\Recorded Data\\InputTest\\");
					File[] listOfFiles = folder.listFiles();

					for (File file : listOfFiles) {
					    if (file.isFile()) {
							JAXBContext context;
							try {
								context = JAXBContext.newInstance(testPlannerModel.getClass());
							

					    	Unmarshaller unmarshaller = context.createUnmarshaller();
					        System.out.println(file.getName());
					        String savePath = System.getProperty("user.dir")+"\\"+"Recorded Data"+"\\"+"InputTest"+"\\"+file.getName();
					        System.out.println(savePath);

							testPlannerModel.set_testPlanItemList(((TestManager) unmarshaller.unmarshal(new FileReader(savePath))).get_testPlanItemList());
					        System.out.println(testPlannerModel);
					        //testPlannerModel.runTests();
					       LinkedList<TestPlanItem> tp= testPlannerModel.get_testPlanItemList();
					       System.out.println(tp.size()); 
					       int i=0;
					       
					       question( i,tp,usabilityTestCaseForm,true);
					      // for (int i=0; i< tp.size();i++){}
							
							
						
					       
							} catch (FileNotFoundException | JAXBException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
					    }
				    
					
				}
				}
//				usabilityTestCaseForm.setUserName(userName_txt.getText());
//				usabilityTestCaseForm.setAge(age_txt.getText());
//				usabilityTestCaseForm.setGazeDataProviderType(spinner_1.getValue().toString());
//				usabilityTestCaseForm.setTargetSelectionType(spinner_3.getValue().toString());
//				usabilityTestCaseForm.setTriggerType(spinner.getValue().toString());
//				usabilityTestCaseForm.setGender(Gender.getValue().toString());
//				System.out.println(usabilityTestCaseForm);
//				TestPlanItem tp =new TestPlanItem();
//				tp.setGazeDataProviderType(GazeDataProviderType.valueOf(spinner_1.getValue().toString()));
//				tp.setNumberOfSelections(5);
//				tp.setTargetSelectionType(TargetSelectionType.valueOf(spinner_3.getValue().toString()));
//				tp.setRepType(RepetitionType.valueOf("SEQUENTIAL"));
//				//tp.setTargetSelectionTypeProperties(targetSelectionTypeProperties);
//				TestPlannerModel tmodel =new TestPlannerModel();
//				LinkedList<TestPlanItem> _testPlanItemList=new LinkedList<TestPlanItem>();
//				System.out.println(tp.getGazeDataProviderType());
//				_testPlanItemList.add(tp);
//				tmodel.set_testPlanItemList(_testPlanItemList);
//				if (!tmodel.get_testPlanItemList().isEmpty()) {
//					System.out.println("herer");
//					tmodel.runTests();
//				}						
						//TestCaseUsability tcu = new TestCaseUsability();
				//tcu.setVisible(true);
				//frmTestForUsability.setVisible(false);
	//	}

			}	
		});
		
	}
	public void question(int i, LinkedList<TestPlanItem> tp,
			UsabilityTestCaseForm usabilityTestCaseForm, boolean frmUsabilityTest) {
		
i++;
    	   TestPlanItem tp1= new TestPlanItem();
		   System.out.println("ssssssss" +i +tp.size());

    	   if (i==tp.size()){
    		   System.out.println("ssssssss");
				TestQuestionnare tq= new TestQuestionnare();
				tq.setVisible(true);
			}else{
    	   tp1=tp.get(i);
			System.out.println("Inside if");
			
		if(tp1.getTargetSelectionType().toString().equalsIgnoreCase(usabilityTestCaseForm.getTargetSelectionType())&&
				tp1.getTargetSelectionTypeProperties().get(4).toString().equalsIgnoreCase(usabilityTestCaseForm.getTriggerType()) &&
				tp1.getGazeDataProviderType().toString().equalsIgnoreCase(usabilityTestCaseForm.getGazeDataProviderType())){
			System.out.println("Inside if");
			
			Question ques=new Question();
					 ques.setquestions(tp1, i, tp, usabilityTestCaseForm);
			
			//if (selectedOption == JOptionPane.YES_OPTION) {
			//testPlannerModel.runSingleTest(tp1);

		ques.setVisible(frmUsabilityTest);
		//ques.setSize(ques.MAXIMIZED_HORIZ, ques.MAXIMIZED_VERT);
		
		}else{
			question( i,tp,usabilityTestCaseForm,frmUsabilityTest);
		}
		System.out.println("ffff "+ i + tp.size());
		
		/*i++;
       if(i<tp.size()){
		question(i,tp,usabilityTestCaseForm);
       }*/
	}
	}
}

