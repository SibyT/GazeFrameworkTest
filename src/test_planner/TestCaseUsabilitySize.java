package test_planner;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.JTree;

public class TestCaseUsabilitySize extends JFrame {

	private JPanel contentPane;
	private JTextField txtSelectButtonIn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestCaseUsabilitySize frame = new TestCaseUsabilitySize();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestCaseUsabilitySize() {
		setTitle("Test Case Usability for Size");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton_4 = new JButton("Next");
		btnNewButton_4.setBounds(335, 202, 89, 23);
		contentPane.add(btnNewButton_4);
		
		txtSelectButtonIn = new JTextField();
		txtSelectButtonIn.setText("Select Button in the order 1,2,3,4");
		txtSelectButtonIn.setBounds(10, 11, 400, 20);
		contentPane.add(txtSelectButtonIn);
		txtSelectButtonIn.setColumns(10);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setToolTipText("Progress");
		progressBar.setString("33%");
		progressBar.setValue(33);
		progressBar.setIndeterminate(true);
		progressBar.setStringPainted(true);
		progressBar.setMinimum(33);
		progressBar.setBounds(60, 236, 350, 14);
		contentPane.add(progressBar);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("New radio button");
		rdbtnNewRadioButton.setBounds(315, 125, 109, 23);
		contentPane.add(rdbtnNewRadioButton);
		
		JToggleButton tglbtnNewToggleButton = new JToggleButton("New toggle button");
		tglbtnNewToggleButton.setBounds(303, 168, 121, 23);
		contentPane.add(tglbtnNewToggleButton);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("New check box");
		chckbxNewCheckBox.setBounds(288, 98, 122, 60);
		contentPane.add(chckbxNewCheckBox);
		
		JSpinner spinner = new JSpinner();
		spinner.setBounds(253, 216, 29, 20);
		contentPane.add(spinner);
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setBounds(27, 202, 17, 48);
		contentPane.add(scrollBar);
		
		JSlider slider = new JSlider();
		slider.setBounds(10, 122, 200, 26);
		contentPane.add(slider);
		
		JTree tree = new JTree();
		tree.setBounds(60, 42, 72, 64);
		contentPane.add(tree);
		
		JSlider slider_1 = new JSlider();
		slider_1.setBounds(161, 217, 200, 26);
		contentPane.add(slider_1);
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setBounds(74, 169, 29, 20);
		contentPane.add(spinner_1);
	}
}
