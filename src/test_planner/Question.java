package test_planner;

import gaze_selection_library.TestManager;
import gaze_selection_library.TestPlanItem;
import gaze_selection_library.UsabilityTestCaseForm;
import gaze_selection_library.Window;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.awt.Window.Type;

public class Question extends JFrame{
	public Question() {
	    this.setExtendedState( getExtendedState() | JFrame.MAXIMIZED_VERT | JFrame.MAXIMIZED_HORIZ);
	    

		setSize(new Dimension(1200, 600));
		setAlwaysOnTop(true);



		
	}
	private final JPanel contentPanel = new JPanel();

	
	
void setquestions(final TestPlanItem tp1, final int i, final LinkedList<TestPlanItem> tp, final UsabilityTestCaseForm usabilityTestCaseForm)	{
	setType(Type.NORMAL);
	
	
	setTitle("Please Follow Below Message");
	getContentPane().setLayout(null);
	getContentPane().setSize(100, 200);
	setBounds(100, 100, 450, 300);
	//getContentPane().setLayout(new BorderLayout());
	contentPanel.setLayout(new FlowLayout());
	contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
	
	JLabel lblNewLabel = new JLabel("*  Please Select");
	lblNewLabel.setBounds(10, 26, 184, 14);
	getContentPane().add(lblNewLabel);
	JLabel lblNewLabel_1 = new JLabel("Selection can be");
	lblNewLabel_1.setBounds(10, 60, 94, 14);
	getContentPane().add(lblNewLabel_1);
	JLabel lblNewLabel_2 = new JLabel("Total Number of Buttons are");
	lblNewLabel_2.setBounds(10, 89, 163, 14);
	getContentPane().add(lblNewLabel_2);
	JLabel lblNewLabel_3 = new JLabel("Selection type should be");
	lblNewLabel_3.setBounds(10, 125, 153, 14);
	getContentPane().add(lblNewLabel_3);
	JButton btnNewButton = new JButton("Ok");
	btnNewButton.addActionListener(new ActionListener() {
		
		public void actionPerformed(ActionEvent arg0 ) {
			setVisible(false);	
			
			TestPlannerModel testPlannerModel = new TestPlannerModel();

			
				       
				    testPlannerModel.runSingleTest(tp1,i,tp,usabilityTestCaseForm);
				       
						//System.out.println("wwwwwwwwwwwwwwwww " + w);
		}
	});
	btnNewButton.setBounds(157, 180, 89, 23);
	getContentPane().add(btnNewButton);
	
	JLabel lblNewLabel_4 = new JLabel("New label");
	lblNewLabel_4.setBounds(200, 26, 317, 14);
	lblNewLabel_4.setText(String.valueOf(tp1.getNumberOfSelections()) + " Button");
	getContentPane().add(lblNewLabel_4);
	System.out.println("tp1 "+tp1.getNumberOfSelections());
	System.out.println("tp1 "+tp1.getTargetSelectionTypeProperties().get(0));
	System.out.println("tp1 "+tp1.getTargetSelectionTypeProperties().get(4));


	
	JLabel lblNewLabel_5 = new JLabel("New label");
	lblNewLabel_5.setBounds(200, 60, 317, 14);
	lblNewLabel_5.setText(tp1.getRepType().toString());

	getContentPane().add(lblNewLabel_5);
	
	JLabel lblNewLabel_6 = new JLabel("New label");
	lblNewLabel_6.setBounds(200, 89, 317, 14);
	lblNewLabel_6.setText(tp1.getTargetSelectionTypeProperties().get(1));

	getContentPane().add(lblNewLabel_6);
	
	JLabel lblNewLabel_7 = new JLabel("New label");
	lblNewLabel_7.setBounds(200, 125, 317, 14);
	lblNewLabel_7.setText(tp1.getTargetSelectionTypeProperties().get(4));
	getContentPane().add(lblNewLabel_7);
	







}}
